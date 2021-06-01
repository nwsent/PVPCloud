package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.blazingpack.bpauth.BlazingPackAuthEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.tools.ToolsPlugin

class BlazingPackAuthListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onBlazingPackAuth(event: BlazingPackAuthEvent) {
        val player: ProxiedPlayer = event.userConnection
                ?: return

        var blacklist = this.plugin.blacklistManager.getBlacklistName(player.name)

        if (blacklist == null) {
            blacklist = this.plugin.blacklistManager.getBlacklistIp(player.address.address.hostAddress)
        }

        if (blacklist == null) {
            blacklist = this.plugin.blacklistManager.getBlacklistComputerUUID(event.computerUUID)
        }

        if (blacklist != null) {
            if (blacklist.ip == "-" || blacklist.computerUUID.contentToString() != this.plugin.blacklistManager.valid) {
                blacklist.ip = player.address.address.hostAddress
                blacklist.computerUUID = event.computerUUID
                blacklist.updateEntity()
            }

            val message = arrayListOf(*plugin.config.messages.formatLoginDisallowBlackList.toTypedArray())
            message.replaceAll { blacklist.replaceString(it) }
            player.disconnect(TextComponent(message.joinToString(separator = "\n").fixColors()))
        }
    }
}