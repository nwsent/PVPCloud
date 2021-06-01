package pl.pvpcloud.guild.api

import pl.pvpcloud.guild.api.structure.Guild
import java.util.*

object GuildsAPI {

    internal lateinit var instance: GuildsBootstrap

    fun getGuildBy(predicate: (Guild) -> Boolean): Guild? = this.instance.guildRepository.getGuildBy(predicate)
    fun getGuildById(id: UUID): Guild? = this.instance.guildRepository.getGuildById(id)
    fun getGuildByTag(tag: String): Guild? = this.instance.guildRepository.getGuildByTag(tag)
    fun getGuildByName(name: String): Guild? = this.instance.guildRepository.getGuildByName(name)
    fun getGuildByMember(uuid: UUID): Guild? = this.instance.guildRepository.getGuildByMember(uuid)

}