package pl.pvpcloud.guild.impl.variable.top

import org.bukkit.entity.Player
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.tablist.TablistAdapter

class TopGuildByPointsVariable(name: String, private val place: Int, private val guildRepository: GuildRepository) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        val guild = this.guildRepository.getGuildByPoints

        if (guild.size >= place) {
            return "&f${guild[place - 1].tag} &8(&e${guild[place - 1].points}&8)"
        }
        return ""
    }

}