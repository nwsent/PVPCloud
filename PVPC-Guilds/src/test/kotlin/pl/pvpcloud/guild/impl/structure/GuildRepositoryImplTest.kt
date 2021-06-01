package pl.pvpcloud.guild.impl.structure

import com.mongodb.MongoClientURI
import org.junit.Before
import pl.pvpcloud.database.api.Database
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.guild.api.structure.GuildRepository
import java.util.logging.Level
import java.util.logging.Logger

class GuildRepositoryImplTest {

    private lateinit var guildRepository: GuildRepository

    @Before
    fun setUp() {
        val mongoLogger = Logger.getLogger("org.mongodb.driver")
        mongoLogger.level = Level.WARNING

        DatabaseAPI.database = Database(MongoClientURI("mongodb://pvp:zaq12wSXc1123@195.201.61.168:27017/pvpcloud"), "pvpcloud")
    }

    @org.junit.Test
    fun getGuildsMap() {
        this.guildRepository = GuildRepositoryImpl()

        println(this.guildRepository.guildsMap.values)
    }
}