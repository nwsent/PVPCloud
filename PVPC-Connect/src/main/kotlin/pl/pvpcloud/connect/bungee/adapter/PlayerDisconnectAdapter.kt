package pl.pvpcloud.connect.bungee.adapter

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import pl.pvpcloud.connect.common.packet.PacketPlayerConnect
import pl.pvpcloud.connect.common.packet.PacketPlayerDelete
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerDisconnectAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerDelete) {
            val player = ProxyServer.getInstance().getPlayer(packet.uuid) ?: return

            player.disconnect(TextComponent("Zostales rozlaczony z serwerem, poniewaz nie ma Cie w bazie danych (0x01 error)"))
        }
    }

}