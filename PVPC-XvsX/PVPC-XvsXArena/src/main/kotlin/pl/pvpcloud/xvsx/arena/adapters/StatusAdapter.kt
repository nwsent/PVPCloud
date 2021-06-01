package pl.pvpcloud.xvsx.arena.adapters

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.common.packets.PacketStatusRequest
import pl.pvpcloud.xvsx.common.packets.PacketStatusResponse

class StatusAdapter(private val plugin: XvsXPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketStatusRequest) {
            val callback = PacketStatusResponse()
            callback.callbackId = packet.callbackId
            NetworkAPI.publish(id) { callback }
        }
    }
}