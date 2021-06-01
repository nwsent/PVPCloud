package pl.pvpcloud.moles.adapters

import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.common.packets.PacketStatusRequest
import pl.pvpcloud.moles.common.packets.PacketStatusResponse
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter

class StatusAdapter(private val plugin: MolesPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketStatusRequest) {
            val callback = PacketStatusResponse()
            callback.callbackId = packet.callbackId
            NetworkAPI.publish(id) { callback }
        }
    }
}