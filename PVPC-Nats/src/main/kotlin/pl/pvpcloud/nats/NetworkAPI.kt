package pl.pvpcloud.nats

import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.nats.api.INatsClient
import pl.pvpcloud.nats.api.NatsPacket

object NetworkAPI {

    internal lateinit var client: INatsClient

    val id: String
        get() = this.client.id

    fun publish(packet: () -> NatsPacket) {
        this.client.publish(packet)
    }

    fun publish(id: String, packet: () -> NatsPacket) {
        this.client.publish(id, packet)
    }

    fun publish(vararg ids: String, packet: () -> NatsPacket) {
        this.client.publish(*ids, packet = packet)
    }

    fun requestAndResponse(id: String, packet: () -> NatsPacket, adapter: (NatsPacket) -> Unit) {
        this.client.requestAndResponse(id, packet, adapter)
    }

    fun subscribe(handler: INatsAdapter) {
        this.client.subscribe(handler)
    }

    fun registerAdapters(vararg adapters: INatsAdapter) {
        adapters.forEach {
            subscribe(it)
        }
    }

}