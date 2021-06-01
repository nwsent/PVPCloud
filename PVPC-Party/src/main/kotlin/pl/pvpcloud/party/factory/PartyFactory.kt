package pl.pvpcloud.party.factory

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendHoverMessageCommand
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.basic.Party
import pl.pvpcloud.party.basic.PartyMember
import pl.pvpcloud.party.event.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class PartyFactory(private val partyModule: PartyModule) {

    private val ai = AtomicInteger(0)

    fun createParty(leader: Player) {
        if (this.partyModule.partyRepository.getPartyByUUID(leader.uniqueId) != null)
            return leader.sendFixedMessage(this.partyModule.config.partyYouAlreadyGotParty)

        val party = Party(this.ai.getAndIncrement(), leader.uniqueId)

        party.members.add(PartyMember(leader.uniqueId, leader.name))
        this.partyModule.partyRepository.parties[party.id] = party
        this.partyModule.plugin.server.pluginManager.callEvent(PartyCreateEvent(leader))
        leader.sendFixedMessage("&8* &aTwoje party zostało stworzone")
    }

    fun deleteParty(player: Player) {
        val party = this.partyModule.partyRepository.getPartyByUUID(player.uniqueId)
                ?: return player.sendFixedMessage(this.partyModule.config.partyYouDontHaveParty)

        if (party.uniqueIdLeader != player.uniqueId)
            return player.sendFixedMessage(this.partyModule.config.partyYouMustBeLeader)

        this.partyModule.partyRepository.parties.remove(party.id)
        this.partyModule.plugin.server.pluginManager.callEvent(PartyDeleteEvent(player))
        party.sendMessage("&8* &cTwoje party zostalo usuniete")
    }

    fun leaveParty(player: Player, quit: Boolean) {
        val party = this.partyModule.partyRepository.getPartyByUUID(player.uniqueId)
                ?: return player.sendFixedMessage(this.partyModule.config.partyYouDontHaveParty)

        if (party.uniqueIdLeader == player.uniqueId && !quit)
            return player.sendFixedMessage(this.partyModule.config.partyYouAreLeader)

        if (party.uniqueIdLeader == player.uniqueId) {
            this.deleteParty(player)
        } else {
            this.partyModule.partyFactory.removeMember(player.uniqueId, party)
            this.partyModule.plugin.server.pluginManager.callEvent(PartyLeaveEvent(player))
            this.partyModule.plugin.server.pluginManager.callEvent(PartyDisconnectEvent(player, party.id))
            party.sendMessage("&8* &fGracz &e${player.name} &fwyszedł z twojego party")
        }
    }

    fun kickPlayerFromParty(leader: Player, kickPlayerName: String) {
        val party = this.partyModule.partyRepository.getPartyByUUID(leader.uniqueId)
                ?: return leader.sendFixedMessage(this.partyModule.config.partyYouDontHaveParty)

        if (party.uniqueIdLeader != leader.uniqueId)
            return leader.sendFixedMessage(this.partyModule.config.partyYouMustBeLeader)

        val kickPlayer = Bukkit.getOfflinePlayer(kickPlayerName)

        if (!party.hasMember(kickPlayer.uniqueId))
            return leader.sendFixedMessage(this.partyModule.config.partyPlayerNotInYoursParty)

        if (party.uniqueIdLeader == kickPlayer.uniqueId) {
            return leader.sendFixedMessage(this.partyModule.config.partyYoyAreLeader)
        } else {
            this.partyModule.partyFactory.removeMember(kickPlayer.uniqueId, party)
            val onlinePlayer = kickPlayer.player
            if (onlinePlayer != null) {
                this.partyModule.plugin.server.pluginManager.callEvent(PartyKickEvent(onlinePlayer))
                this.partyModule.plugin.server.pluginManager.callEvent(PartyDisconnectEvent(onlinePlayer, party.id))
            }
            party.sendMessage("&8* &fGracz &e${kickPlayer.name} &fzostał wyrzucony z twojego party")
        }
    }

    fun addMember(player: Player, leaderName: String) {
        if (this.partyModule.partyRepository.getPartyByUUID(player.uniqueId) != null)
            return player.sendFixedMessage(this.partyModule.config.partyYouAlreadyGotParty)

        val party = this.partyModule.partyRepository.getPartyByUUID(Bukkit.getOfflinePlayer(leaderName).uniqueId)
                ?: return player.sendFixedMessage(this.partyModule.config.partyPlayerDontHaveParty)

        if (!partyModule.partyFactory.hasInvite(player.uniqueId, party))
            return player.sendFixedMessage(this.partyModule.config.partyYouDontHaveInvite)
        if (party.members.size >= this.partyModule.maxMembers) {
            return player.sendFixedMessage(this.partyModule.config.partyHasMaxMembers)
        }
        party.members.add(PartyMember(player.uniqueId, player.name))
        this.partyModule.plugin.server.pluginManager.callEvent(PartyJoinEvent(player, party.id))
        party.sendMessage("&8* &fGracz &e${player.name} &fdołączył do twojego party")
    }

    fun removeMember(player: UUID, party: Party) {
        val member = party.members.single { it.uniqueId == player }
        party.members.remove(member)
    }

    fun invitePlayer(leader: Player, invite: String) {
        val party = this.partyModule.partyRepository.getPartyByUUID(leader.uniqueId)
                ?: return leader.sendFixedMessage(this.partyModule.config.partyYouDontHaveParty)

        if (party.uniqueIdLeader != leader.uniqueId)
            return leader.sendFixedMessage(this.partyModule.config.partyYouMustBeLeader)
        if (leader.name == invite) {
            return
        }

        val invitedPlayer = Bukkit.getPlayer(invite)
                ?: return leader.sendFixedMessage(this.partyModule.config.playerOffline)

        party.invites.add(invitedPlayer.uniqueId)

        leader.sendFixedMessage("&8* &fZaprosiłeś gracza &e${invitedPlayer.name} &fdo twojego party")
        invitedPlayer.sendHoverMessageCommand("&8* &eZostałeś zaproszony do party &e&lDOLĄCZ", "&7Kliknij aby dołączyć.", "/party dolacz ${leader.name}")
    }

    fun hasInvite(invite: UUID, party: Party): Boolean {
        return party.invites.contains(invite)
    }
}