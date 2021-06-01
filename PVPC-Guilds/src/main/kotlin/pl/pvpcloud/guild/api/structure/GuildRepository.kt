package pl.pvpcloud.guild.api.structure

import java.util.*

interface GuildRepository {

    val guildsMap: MutableMap<UUID, Guild>
    val guildRecruitment: MutableMap<UUID, Guild>

    fun getGuildBy(predicate: (Guild) -> Boolean): Guild?

    val getGuildByPoints get() = this.guildsMap.values.sortedByDescending { it.points }
    val getGuildByKills get() = this.guildsMap.values.sortedByDescending { it.kills }
    val getGuildByDeaths get() = this.guildsMap.values.sortedByDescending { it.deaths }

    fun getGuildById(id: UUID): Guild? = this.getGuildBy { it.guildId == id }
    fun getGuildByMember(uuid: UUID): Guild? = this.getGuildBy { it.members.singleOrNull { member -> member.uuid == uuid } != null }
    fun getGuildByTag(tag: String): Guild? = this.getGuildBy { it.tag.equals(tag, true) }
    fun getGuildByName(name: String): Guild? = this.getGuildBy { it.name.equals(name, true) }
    fun getGuildPosition(tag: String): Int {
        for ((index, guild) in this.getGuildByPoints.withIndex()) {
            if (guild.tag == tag) {
                return index + 1
            }
        }
        return Int.MAX_VALUE
    }

}