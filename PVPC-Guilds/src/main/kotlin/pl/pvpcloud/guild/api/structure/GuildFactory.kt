package pl.pvpcloud.guild.api.structure

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

interface GuildFactory {

    fun createGuild(id: UUID, tag: String, name: String, leaderUUID: UUID, leaderName: String)
    fun removeGuild(id: UUID, who: UUID): Boolean
    fun timeOutGuild(guild: Guild): Boolean

    fun addMember(uuid: UUID, name: String, target: Guild): Boolean
    fun removeMember(uuid: UUID, target: Guild, who: UUID): Boolean

    fun inviteMember(uuid: UUID, target: Guild)

    fun inviteAlly(sender: UUID, target: UUID)
    fun acceptAlly(sender: UUID, target: UUID)
    fun removeAlly(sender: UUID, target: UUID)

    fun getGuildTagFor(sender: Player, receiver: Player): String

    fun getRelationColor(guild: Guild, target: Guild): ChatColor {
        return when (getRelation(guild, target)) {
            GuildRelation.OWN -> ChatColor.GREEN
            GuildRelation.ALLY -> ChatColor.GOLD
            GuildRelation.ENEMY -> ChatColor.RED
        }
    }

    fun getRelation(guild: Guild, target: Guild): GuildRelation {
        return when {
            guild.guildId == target.guildId -> GuildRelation.OWN
            guild.isAlly(target) -> GuildRelation.ALLY
            else -> GuildRelation.ENEMY
        }
    }

}