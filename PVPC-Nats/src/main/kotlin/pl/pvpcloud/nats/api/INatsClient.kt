package pl.pvpcloud.nats.api

interface INatsClient {

    val id: String

    fun connect()
    fun disconnect()

    fun publish(packet: () -> NatsPacket)
    fun publish(id: String, packet: () -> NatsPacket)
    fun publish(vararg ids: String, packet: () -> NatsPacket)

    fun requestAndResponse(id: String, packet: () -> NatsPacket, adapter: (NatsPacket) -> Unit)

    fun subscribe(handler: INatsAdapter)

}