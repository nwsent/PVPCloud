package pl.pvpcloud.connect.bukkit.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.connect.common.packet.PacketPlayerActionbar
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerActionbarAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerActionbar) {
            val player = Bukkit.getPlayer(packet.uuid) ?: return
            player.sendActionBar(packet.message)
        }
    }

}