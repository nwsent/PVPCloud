package pl.pvpcloud.redis

import io.lettuce.core.api.sync.RedisCommands

object RedisAPI {

    internal lateinit var instance: RedisClient

    fun getCommands(block: (RedisCommands<String, Any>) -> Unit) {
        this.instance.getCommands(block)
    }

}