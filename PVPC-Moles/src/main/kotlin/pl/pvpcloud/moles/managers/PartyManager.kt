package pl.pvpcloud.moles.managers

import com.google.common.cache.CacheBuilder
import org.bukkit.entity.Player
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.party.Party
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class PartyManager(private val plugin: MolesPlugin) {

    private val parties = HashMap<UUID, Party>() //uuid party leader
    private val partiesPlayers = HashMap<UUID, UUID>() //uuid player, uuid leader
    private val invites = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(45, TimeUnit.SECONDS)
            .build<UUID, UUID>() // InvitePlayer, leader

    fun getParty(uuid: UUID): Party? {
        return this.parties[uuid] ?: this.parties[this.partiesPlayers[uuid]]
    }

    fun hasInvite(uniqueIdLeader: UUID, uniqueIdRequester: UUID): Boolean {
        val uuidLeader = this.invites.getIfPresent(uniqueIdRequester)
            ?: return false
        return uniqueIdLeader == uuidLeader
    }

    fun createParty(party: Party) {
        this.parties[party.leader] = party
    }

    fun deleteParty(party: Party) {
        this.parties.remove(party.leader)
        party.members.forEach {
            this.partiesPlayers.remove(it)
        }
    }

    fun leaveParty(uniqueId: UUID) {
        this.partiesPlayers.remove(uniqueId)
    }

    fun joinToParty(player: Player, party: Party) {
        this.partiesPlayers[player.uniqueId] = party.leader
    }

    fun inviteToParty(invited: Player, party: Party) {
        this.invites.put(invited.uniqueId, party.leader)
    }
}