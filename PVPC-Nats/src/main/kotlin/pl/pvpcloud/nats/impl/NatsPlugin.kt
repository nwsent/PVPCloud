package pl.pvpcloud.nats.impl

import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.codec.MsgPackCodec

class BukkitNatsPlugin : JavaPlugin() {

    lateinit var config: NatsConfig

    init {
        System.setProperty("protostuff.runtime.allow_null_array_element", "true")
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true")
    }

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, NatsConfig::class, "config")

        val client = NatsClient(
                MsgPackCodec(),
                this.config.id,
                this.config.url,
                this.config.username,
                this.config.password
        )

        client.connect()

        NetworkAPI.client = client
    }

    override fun onDisable() {
        NetworkAPI.client.disconnect()
    }

}

class BungeeNatsPlugin : Plugin() {

    lateinit var config: NatsConfig

    init {
        System.setProperty("protostuff.runtime.allow_null_array_element", "true")
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true")
    }

    override fun onEnable() {
        this.config = pl.pvpcloud.common.configuration.ConfigLoader.load(this.dataFolder, NatsConfig::class, "config")

        val client = NatsClient(
                MsgPackCodec(),
                this.config.id,
                this.config.url,
                this.config.username,
                this.config.password
        )

        client.connect()

        NetworkAPI.client = client
    }

    override fun onDisable() {
        NetworkAPI.client.disconnect()
    }

}

class NatsConfig {

    val url: String = "localhost"
    val username: String = "username"
    val password: String = "password"
    val id: String = "proxy-sector-1"

    override fun toString(): String {
        return "NatsConfig(url='$url', username='$username', password='$password', id='$id')"
    }


}