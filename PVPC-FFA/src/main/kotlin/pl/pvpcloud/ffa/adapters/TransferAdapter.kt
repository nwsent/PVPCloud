package pl.pvpcloud.ffa.adapters

import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.packets.hub.PacketTransfer

class TransferAdapter(private val plugin: FFAPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketTransfer) {
            this.plugin.arenaManager.handleTransferPacket(packet)
        }
    }
}