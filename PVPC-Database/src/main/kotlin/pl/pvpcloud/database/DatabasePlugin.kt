package pl.pvpcloud.database

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.Bukkit
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.database.api.Database
import pl.pvpcloud.database.api.DatabaseCredentials
import java.util.logging.Level.WARNING
import java.util.logging.Logger

class DatabasePluginBukkit : CloudPlugin() {

    lateinit var config: DatabaseConfig
    lateinit var database: Database

    //mongodb://pvpbonsko:pvpcloud@cluster0-shard-00-01-kk4dj.mongodb.net:27017/?streamType=netty&ssl=true dziala

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, DatabaseConfig::class, "config")
        val databaseCredentials = DatabaseCredentials(this.config.url, this.config.database)
        logger.info("Connecting to mongodb...")
        logger.info("Host: ${databaseCredentials.url} Database: ${databaseCredentials.databaseName}")
        try {
            this.database = Database(databaseCredentials)
        } catch (e: Exception) {
            logger.warning("Can't connect to database, disabling server.")
            e.printStackTrace()
            Bukkit.shutdown()
        }
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = WARNING
    }
}

class DatabasePluginBungee : Plugin() {

    lateinit var config: DatabaseConfig
    lateinit var database: Database

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, DatabaseConfig::class, "config")
        val databaseCredentials = DatabaseCredentials(this.config.url, this.config.database)
        logger.info("Connecting to mongodb...")
        logger.info("Host: ${databaseCredentials.url} Database: ${databaseCredentials.databaseName}")
        try {
            this.database = Database(databaseCredentials)
        } catch (e: Exception) {
            logger.warning("Can't connect to database, disabling server.")
            ProxyServer.getInstance().stop()
        }
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = WARNING
    }
}

class DatabaseConfig {
    val url = "mongodb://pvpcloud:UtlumiOnVondavekIkmamFijoaryet@127.0.0.1:27017/pvpcloud"
    val database = "pvpcloud"
}