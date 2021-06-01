package pl.pvpcloud.guild.impl.structure

import pl.pvpcloud.database.api.DatabaseEntity
import pl.pvpcloud.guild.api.structure.Guild
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashSet

data class GuildImpl(
        override val guildId: UUID,
        override val tag: String,
        override val name: String,
        override var leaderUUID: UUID
) : DatabaseEntity(), Guild, Serializable {

    override var kills: Int = 0
    override var deaths: Int = 0
    override var assists: Int = 0
    override var points: Int = 500

    override var coins: Int = 0

    override var pvp: Boolean = true

    override var timeGuild: Long = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)

    override var timeRecruitment: Long = System.currentTimeMillis() + 0L
    override var messageRecruitment: String = ""

    override val invites: MutableSet<UUID> = HashSet()
    override var alliesInvites: MutableSet<UUID> = HashSet()
    override var allies: MutableSet<UUID> = HashSet()
    override val members: MutableSet<GuildMemberImpl> = HashSet()

    override val id: String
        get() = this.guildId.toString()
    override val collection: String
        get() = "guilds"
    override val key: String
        get() = "guildId"

    override fun toString(): String {
        return "GuildImpl(guildId=$guildId, tag='$tag', name='$name', leaderUUID=$leaderUUID, kills=$kills, deaths=$deaths, assists=$assists, points=$points, pvp=$pvp, invites=$invites, alliesInvites=$alliesInvites, allies=$allies, members=$members)"
    }


}