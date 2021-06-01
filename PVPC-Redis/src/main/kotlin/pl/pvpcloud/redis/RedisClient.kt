package pl.pvpcloud.redis

import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands

class RedisClient(
        private val hostname: String,
        private val password: String,
        private val database: Int,
        private val port: Int
) {

    private lateinit var client: io.lettuce.core.RedisClient
    private lateinit var connection: StatefulRedisConnection<String, Any>
    private lateinit var commands: RedisCommands<String, Any>

    fun connect() {
        this.client = io.lettuce.core.RedisClient.create(
                RedisURI.builder()
                        .withHost(this.hostname)
                        .withPassword(this.password)
                        .withDatabase(this.database)
                        .withPort(this.port)
                        .build()
        )

        this.connection = this.client.connect(RedisCodec())
        this.commands = this.connection.sync()
    }

    fun disconnect() {
        this.connection.close()
    }

    fun getCommands(block: (RedisCommands<String, Any>) -> Unit) {
        block(this.commands)
    }

}