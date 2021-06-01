package pl.pvpcloud.nats.impl

import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.nats.api.codec.ProtostuffCodec

object SecondAdapter : INatsAdapter {

    private lateinit var client: NatsClient

    @JvmStatic
    fun main(args: Array<String>) {
        this.client = NatsClient(ProtostuffCodec(8192), "pvpc-network-2", "nats://localhost:8000", "opensectors", "password")
        this.client.connect()
        this.client.subscribe(SecondAdapter)
    }

    private var index: Int = 0

    override fun received(id: String, packet: Any) {
        if (this.index++ >= 2000) {
            println(packet)
        }
    }

}