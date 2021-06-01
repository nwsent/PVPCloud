package pl.pvpcloud.guild.api.structure

import pl.pvpcloud.guild.impl.structure.GuildMemberImpl
import java.io.Serializable
import java.util.*

interface Guild : Serializable {

    val guildId: UUID
    val tag: String
    val name: String
    var leaderUUID: UUID

    var kills: Int
    var deaths: Int
    var assists: Int
    var points: Int

    var coins: Int

    var pvp: Boolean

    var timeGuild: Long

    var timeRecruitment: Long
    var messageRecruitment: String

    val invites: MutableSet<UUID>
    var alliesInvites: MutableSet<UUID>
    var allies: MutableSet<UUID>
    val members: MutableSet<GuildMemberImpl>

    fun isAlly(guild: Guild) = this.allies.contains(guild.guildId)
    fun isMember(member: UUID) = this.members.singleOrNull { it.uuid == member } != null

    fun getMember(member: UUID) = this.members.singleOrNull { it.uuid == member }!!

    fun isLeader(uniqueId: UUID) = (this.leaderUUID == uniqueId)

    fun hasPermission(uniqueId: UUID, permission: String): Boolean = (this.leaderUUID == uniqueId || getMember(uniqueId).hasPermission(permission))

    fun getLeader() = this.members.singleOrNull { it.uuid == this.leaderUUID }!!

}