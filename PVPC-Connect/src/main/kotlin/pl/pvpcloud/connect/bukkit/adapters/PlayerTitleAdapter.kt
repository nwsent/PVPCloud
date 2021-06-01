package pl.pvpcloud.connect.bukkit.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.connect.common.packet.PacketPlayerTitle
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerTitleAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerTitle) {
            val player = Bukkit.getPlayer(packet.uuid) ?: return
            player.sendTitle(packet.title, packet.subtitle, 20, 100, 20)
        }
    }

}