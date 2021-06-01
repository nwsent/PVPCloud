package pl.pvpcloud.nats.impl

import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.nats.api.codec.ProtostuffCodec
import java.util.*

object FirstAdapter : INatsAdapter {

    private lateinit var client: NatsClient

    @JvmStatic
    fun main(args: Array<String>) {
        this.client = NatsClient(ProtostuffCodec(8192), "pvpc-network-1", "nats://localhost:8000", "opensectors", "password")
        this.client.connect()
        this.client.subscribe(FirstAdapter)

        val startTime = System.currentTimeMillis()

        for (index in 0..1_000_000) {
            this.client.publish("pvpc-network-1") {
                PacketPlayerMessage(UUID.randomUUID(), arrayOf("cyramek to chuj"))
            }
        }

        println("PING-PONG TIME = ${System.currentTimeMillis() - startTime}")
    }

    @Volatile
    private var index: Int = 0

    override fun received(id: String, packet: Any) {
        if (this.index++ >= 1_000_000) {
            println(packet)
        }
    }

}