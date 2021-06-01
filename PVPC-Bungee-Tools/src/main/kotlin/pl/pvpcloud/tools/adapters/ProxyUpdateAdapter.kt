package pl.pvpcloud.tools.adapters

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketProxyUpdate

class ProxyUpdateAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (id != NetworkAPI.id) {
            if (packet is PacketProxyUpdate) {
                this.plugin.config.whiteListStatus = packet.whiteListStatus
                this.plugin.config.whiteListReason = packet.whiteListReason
                this.plugin.config.whiteListPlayers = packet.whiteListPlayers
                this.plugin.config.description = packet.description
                this.plugin.config.slotMaxShow = packet.slotMaxShow
                this.plugin.config.slotMaxOnline = packet.slotMaxOnline
                this.plugin.saveConfig()
            }
        }
    }
}