package pl.pvpcloud.tools.adapters

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerUpdate

class PlayerUpdateAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerUpdate && id != NetworkAPI.id) {
            this.plugin.userManager.addUser(packet.user)
        }
    }
}