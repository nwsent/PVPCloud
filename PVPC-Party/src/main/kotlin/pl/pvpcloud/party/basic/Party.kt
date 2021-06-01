package pl.pvpcloud.party.basic

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import java.util.*
import kotlin.collections.LinkedHashSet

class Party(
        val id: Int,
        val uniqueIdLeader: UUID
) {

    val createTime: Long = System.currentTimeMillis()

    val invites: MutableSet<UUID> = hashSetOf()
    val members: MutableSet<PartyMember> = LinkedHashSet()

    var partyState: PartyState = PartyState.CLOSED

    fun membersOnline() =
            this.members.asSequence().mapNotNull { it.player }

    fun hasMember(uniqueId: UUID) =
            this.members.any { it.uniqueId == uniqueId }

    fun getMember(uniqueId: UUID) =
            this.members.singleOrNull { it.uniqueId == uniqueId } ?: throw NullPointerException("Party member is null :O")

    fun sendMessage(message: String) {
        this.members.mapNotNull { Bukkit.getPlayer(it.uniqueId) }.forEach { it.sendFixedMessage(message) }
    }

    fun sendMessage(message: Array<String>) {
        this.members.mapNotNull { Bukkit.getPlayer(it.uniqueId) }.forEach { it.sendFixedMessage(message) }
    }

    val leaderMember
            get() = this.members.first()
}