package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.BanType
import java.util.concurrent.TimeUnit

class PreLoginListener(private val plugin: ToolsPlugin) : Listener {

    private val connection = HashMap<String, Long>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPreLogin(event: PreLoginEvent) {

        val name = event.connection.name

        if (plugin.config.whiteListStatus) {

            if (!plugin.config.whiteListPlayers.contains(name.toLowerCase())) {
                event.isCancelled = true
                event.setCancelReason(TextComponent(plugin.config.whiteListReason.fixColors()))
            }
        }

        if (this.plugin.config.slotMaxOnline <= ProxyServer.getInstance().onlineCount && !plugin.config.whiteListPlayers.contains(name.toLowerCase())) {
            val message = arrayListOf(*plugin.config.messages.serverIsFull.toTypedArray())
            event.isCancelled = true
            event.setCancelReason(TextComponent(message.joinToString { "\n" + it }.fixColors()))
        }

        val lastConnect = connection[name]

        if (lastConnect != null && lastConnect >= System.currentTimeMillis()) {
            event.setCancelReason(TextComponent(this.plugin.config.messages.youLoggingToFast.rep("%time%", TimeUnit.MILLISECONDS.toSeconds(lastConnect - System.currentTimeMillis()).toString()).fixColors()))
            event.isCancelled = true
            return
        }
        this.connection[name] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(4)

        if (!ToolsAPI.isAllowConnect()) {
            event.setCancelReason(TextComponent("&cPoczekaj, zbyt duzo osob chce wejsc na serwer!".fixColors()))
            event.isCancelled = true
            return
        }

        val ip = event.connection.address.address.hostAddress
        val ipBan = this.plugin.banManager.getBan(ip, BanType.IP)
        val idBan = this.plugin.banManager.getBan(event.connection.name, BanType.PLAYER)

        val ban = ipBan ?: idBan ?: return
        val message = arrayListOf(*plugin.config.messages.formatLoginDisallowBanned.toTypedArray())
        message.replaceAll { ban.replaceString(it) }
        event.setCancelReason(TextComponent(message.joinToString(separator = "\n").fixColors()))
        event.isCancelled = true
    }
}