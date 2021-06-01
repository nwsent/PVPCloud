package pl.pvpcloud.database

import com.mongodb.MongoClientURI
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.database.api.Database
import pl.pvpcloud.database.api.DatabaseAPI
import java.util.logging.Level.WARNING
import java.util.logging.Logger

class DatabasePluginBukkit : JavaPlugin() {

    lateinit var database: Database
    lateinit var config: DatabaseConfig

    //mongodb://pvpbonsko:pvpcloud@cluster0-shard-00-01-kk4dj.mongodb.net:27017/?streamType=netty&ssl=true dziala

    init {
        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
    }

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, DatabaseConfig::class, "config")

        try {
            this.database = Database(MongoClientURI(this.config.url), this.config.database)
        } catch (e: Exception) {
            logger.warning("Can't connect to database, disabling server.")
            e.printStackTrace()
            Bukkit.shutdown()
        }
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = WARNING

        DatabaseAPI.database = this.database
    }
}

class DatabasePluginBungee : Plugin() {

    lateinit var database: Database
    lateinit var config: DatabaseConfig

    init {
        System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
    }

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, DatabaseConfig::class, "config")

        try {
            this.database = Database(MongoClientURI(this.config.url), this.config.database)
        } catch (e: Exception) {
            logger.warning("Can't connect to database, disabling server.")
            e.printStackTrace()
        }
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = WARNING

        DatabaseAPI.database = this.database
    }
}

class DatabaseConfig {
    val url = "mongodb://pvpcloud:UtlumiOnVondavekIkmamFijoaryet@127.0.0.1:27017/pvpcloud"
    val database = "pvpcloud"
}