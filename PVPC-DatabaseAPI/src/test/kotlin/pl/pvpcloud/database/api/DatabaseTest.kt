package pl.pvpcloud.database.api

import com.mongodb.MongoClientURI
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class DatabaseTest {

    private val guild: Guild = Guild(UUID.fromString("c5edaf5e-de6f-4da6-919a-10410b9a070e"), "CRMK", "Zajace hasajace po lace")

    @Before
    fun setUp() {
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = Level.WARNING

        DatabaseAPI.database = Database(MongoClientURI("mongodb://pvp:zaq12wSXc1123@195.201.61.168:27017/pvpcloud"), "pvpcloud")
    }

    @Test
    fun insertEntity() {
        this.guild.insertEntity()
    }

    @Test
    fun updateEntity() {
        this.guild.tag = "HASA"
        this.guild.name = "Zajace hasajace po lace 2"

        this.guild.updateEntity()
    }

    @Test
    fun load() {
        DatabaseAPI.load<Guild>("guilds-xd", "c5edaf5e-de6f-4da6-919a-10410b9a070e") {
            println(it)
        }
    }

    @Test
    fun loadBySelector() {
        DatabaseAPI.loadBySelector<Guild>("guilds-xd", "guildId", "c5edaf5e-de6f-4da6-919a-10410b9a070e") {
            println(it)
        }
    }

    @Test
    fun loadAll() {
        DatabaseAPI.loadAll<Guild>("guilds-xd") {
            it.forEach { guild ->
                println(guild)
            }
        }
    }

    @Test
    fun deleteEntity() {
        this.guild.deleteEntity()
    }

    private data class Guild(val guildId: UUID, var tag: String, var name: String) : DatabaseEntity() {

        override val id: String
            get() = this.guildId.toString()

        override val key: String
            get() = "guildId"

        override val collection: String
            get() = "guilds-xd"

    }

}