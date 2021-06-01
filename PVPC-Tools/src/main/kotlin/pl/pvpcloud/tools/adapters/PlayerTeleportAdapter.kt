package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.packets.PacketPlayerTeleport

class PlayerTeleportAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerTeleport) {
            val fromPlayer = Bukkit.getPlayer(packet.fromUUID) ?: return
            val toPlayer = Bukkit.getPlayer(packet.toUUID) ?: return

            fromPlayer.teleport(toPlayer)
        }
    }

}