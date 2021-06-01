package pl.pvpcloud.guild.impl.variable.profil

import org.bukkit.entity.Player
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.tablist.TablistAdapter

class GuildPointsVariable(name: String, private val guildRepository: GuildRepository) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        val guild = GuildsAPI.getGuildByMember(player.uniqueId)
                ?: return ""

        return " &8* &7Ranking: &f${guild.points} &8(&e#&f${this.guildRepository.getGuildPosition(guild.tag)}&8)"
    }

}