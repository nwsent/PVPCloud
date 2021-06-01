package pl.pvpcloud.connect.bukkit.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.common.packet.PacketPlayerMessage
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerMessageAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerMessage) {
            val player = Bukkit.getPlayer(packet.uuid) ?: return
            player.sendFixedMessage(packet.message)
        }
    }

}