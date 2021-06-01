package pl.pvpcloud.moles.party

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.profile.ProfileState
import java.util.*

class Party(
    val plugin: MolesPlugin,
    val leader: UUID
) {
    val members: HashSet<UUID> = hashSetOf(leader)
    val maxMembers: Int = 8 //todo pobierac od rangi
    var partyState: PartyState = PartyState.CLOSED

    fun getPlayers(): List<Player> {
        return this.members.mapNotNull { Bukkit.getPlayer(it) }
    }

    fun sendMessage(message: String) {
        this.getPlayers().forEach { it.sendFixedMessage(message) }
    }

    fun allPlayersState(profileState: ProfileState): Boolean {
        this.members.forEach {
            val profile = this.plugin.profileManager.getProfile(it)
            if (profile.profileState != profileState) return false
        }
        return true
    }

    fun isLeader(uuid: UUID): Boolean {
        return this.leader == uuid
    }
}