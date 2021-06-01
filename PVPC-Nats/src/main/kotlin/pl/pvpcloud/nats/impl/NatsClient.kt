package pl.pvpcloud.nats.impl

import io.nats.client.Connection
import io.nats.client.Nats
import io.nats.client.Options
import pl.pvpcloud.nats.api.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

class NatsClient(
        private val codec: INatsCodec,
        override val id: String,
        private val url: String,
        private val username: String,
        private val password: String
) : INatsClient {

    init {
        System.setProperty("protostuff.runtime.allow_null_array_element", "true")
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true")
    }

    @Volatile
    private lateinit var connection: Connection

    private val futures: MutableMap<Long, CompletableFuture<NatsPacket>> = ConcurrentHashMap()
    private val adapters: MutableSet<INatsAdapter> = ConcurrentHashMap.newKeySet()

    override fun connect() {
        this.connection = Nats.connect(
                Options.Builder()
                        .connectionName(this.id)
                        .server(this.url)
                        .userInfo(
                                this.username.toCharArray(),
                                this.password.toCharArray()
                        )
                        .maxReconnects(10)
                        .noRandomize()
                        .build()
        )

        this.connection.createDispatcher {
            val packet = this@NatsClient.codec.decode(it.data)
            val callback = packet.packet

            if (callback is NatsPacket) {
                this@NatsClient.futures[callback.callbackId]?.complete(callback)
                this@NatsClient.futures.remove(callback.callbackId)
            }

            for (adapter in this@NatsClient.adapters) {
                adapter.received(it.replyTo, callback)
            }
        }.subscribe("pvpc-network-all").subscribe("pvpc-network-${this.id}")
    }

    override fun disconnect() {
        this.connection.close()
    }

    override fun publish(packet: () -> NatsPacket) {
        this.connection.publish("pvpc-network-all", this.id, this.codec.encode(PacketWrapper(packet())))
    }

    override fun publish(id: String, packet: () -> NatsPacket) {
        this.connection.publish("pvpc-network-$id", this.id, this.codec.encode(PacketWrapper(packet())))
    }

    override fun publish(vararg ids: String, packet: () -> NatsPacket) {
        val byteArray = this.codec.encode(PacketWrapper(packet()))

        for (id in ids) {
            this.connection.publish("pvpc-network-$id", this.id, byteArray)
        }
    }

    override fun requestAndResponse(id: String, packet: () -> NatsPacket, adapter: (NatsPacket) -> Unit) {
        val future = CompletableFuture<NatsPacket>()
        future.thenAccept(adapter)

        val pkt = packet()
        this.futures[pkt.callbackId] = future
        this.connection.publish("pvpc-network-$id", this.id, this.codec.encode(PacketWrapper(pkt)))
    }

    override fun subscribe(handler: INatsAdapter) {
        this.adapters.add(handler)
    }

}