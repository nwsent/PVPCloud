package pl.pvpcloud.connect.bungee.adapter

import net.md_5.bungee.api.ProxyServer
import pl.pvpcloud.connect.common.packet.PacketPlayerConnect
import pl.pvpcloud.nats.api.INatsAdapter

class PlayerConnectAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerConnect) {
            val player = ProxyServer.getInstance().getPlayer(packet.uuid) ?: return
            val serverInfo = ProxyServer.getInstance().getServerInfo(packet.id) ?: return

            player.connect(serverInfo)
        }
    }

}