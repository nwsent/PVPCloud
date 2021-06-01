package pl.pvpcloud.guild.impl.structure

import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.guild.api.structure.Guild
import pl.pvpcloud.guild.api.structure.GuildRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GuildRepositoryImpl : GuildRepository {

    override val guildsMap: MutableMap<UUID, Guild> = ConcurrentHashMap()
    override val guildRecruitment: MutableMap<UUID, Guild> = ConcurrentHashMap()


    init {
        DatabaseAPI.loadAll<GuildImpl>("guilds") {
            it.forEach { guild ->

                this.guildsMap[guild.guildId] = guild
                if (guild.timeRecruitment > 1L) {
                    this.guildRecruitment[guild.guildId] = guild
                }

            }
        }

    }

    override fun getGuildBy(predicate: (Guild) -> Boolean): Guild? {
        return this.guildsMap.values.find(predicate)
    }

}