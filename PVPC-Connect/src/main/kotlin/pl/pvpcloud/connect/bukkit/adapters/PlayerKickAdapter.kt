package pl.pvpcloud.connect.bukkit.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.connect.common.packet.PacketPlayerKick
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerKickAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerKick) {
            val player = Bukkit.getPlayer(packet.uuid) ?: return
            player.kickPlayer(packet.message.fixColors())
        }
    }
}