package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.tools.ToolsPlugin

class ProxyPingListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onPing(event: ProxyPingEvent) {
        val ping = event.response

        ping.version = ServerPing.Protocol("BlazingPack 1.8.8", 47)

        val descriptionBuilder = StringBuilder()
        this.plugin.config.description.forEach { descriptionBuilder.append(it).append("\n") }

        ping.descriptionComponent = TextComponent(descriptionBuilder.toString().fixColors())

        val playerInfoList = arrayListOf(*this.plugin.config.playerInfo.toTypedArray())
        playerInfoList.replaceAll { replaceString(it) }

        val sample = arrayOf(
                ServerPing.PlayerInfo(playerInfoList.joinToString(separator = "\n").fixColors(), "")
        )

        val online = ConnectAPI.getPlayers().values.count()

        if (online >= this.plugin.config.slotMaxShow) {
            ping.players.max = this.plugin.config.slotMaxShow + 1
            ping.players.online = this.plugin.config.slotMaxShow
        } else {
            ping.players.max = this.plugin.config.slotMaxShow
            ping.players.online = online
        }
        ping.players.sample = sample

        event.response = ping
    }

    private fun replaceString(string: String) = string
            .rep("%maxOnline%", this.plugin.config.slotMaxShow.toString())
}