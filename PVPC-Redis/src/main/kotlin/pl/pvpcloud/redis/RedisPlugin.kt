package pl.pvpcloud.redis

import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.common.configuration.ConfigLoader

class BukkitRedisPlugin : JavaPlugin() {

    lateinit var config: RedisConfig

    override fun onLoad() {
        this.config = ConfigLoader.load(this.dataFolder, RedisConfig::class, "config")

        RedisAPI.instance = RedisClient(
                this.config.hostname,
                this.config.password,
                this.config.database,
                this.config.port
        )

        RedisAPI.instance.connect()
    }

    override fun onDisable() {
        RedisAPI.instance.disconnect()
    }

}

class BungeeRedisPlugin : Plugin() {

    lateinit var config: RedisConfig

    override fun onEnable() {
        this.config = pl.pvpcloud.common.configuration.ConfigLoader.load(this.dataFolder, RedisConfig::class, "config")

        RedisAPI.instance = RedisClient(
                this.config.hostname,
                this.config.password,
                this.config.database,
                this.config.port
        )

        RedisAPI.instance.connect()
    }

    override fun onDisable() {
        RedisAPI.instance.disconnect()
    }

}

class RedisConfig {
    val hostname: String = "localhost"
    val password: String = "password"
    val database: Int = 0
    val port: Int = 6379

}