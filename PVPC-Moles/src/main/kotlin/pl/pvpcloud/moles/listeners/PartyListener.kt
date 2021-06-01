package pl.pvpcloud.moles.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendHoverMessageCommand
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.party.Party
import pl.pvpcloud.moles.party.event.*
import pl.pvpcloud.moles.profile.ProfileState

class PartyListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onCreate(event: PartyCreateEvent) {
        val player = event.sender
        if (this.plugin.partyManager.getParty(player.uniqueId) != null)
            return player.sendFixedMessage(this.plugin.config.messages.partyYouAlreadyGotParty)

        if (this.plugin.profileManager.isBusy(player))
            return player.sendFixedMessage(this.plugin.config.messages.partyYouAreBusy)

        val party = Party(this.plugin, player.uniqueId)
        Bukkit.getScheduler().runTaskLater(this.plugin, { this.plugin.profileManager.refreshHotbar(player) }, 2)
        this.plugin.partyManager.createParty(party)
        player.sendFixedMessage("&8* &aTwoje party zostało stworzone")
    }

    @EventHandler
    fun onDelete(event: PartyDeleteEvent) {
        val player = event.sender
        val party = this.plugin.partyManager.getParty(player.uniqueId)
                ?: return player.sendFixedMessage(this.plugin.config.messages.partyYouDontHaveParty)
        val profile = this.plugin.profileManager.getProfile(player)

        when (profile.profileState) {
            ProfileState.FIGHTING ->
                return player.sendFixedMessage("&4Upsik! &fParty możesz tylko usunąć w lobby")
            ProfileState.LOADING ->
                return player.sendFixedMessage("&4Upsik! &fParty możesz tylko usunąć w lobby")
            ProfileState.LOBBY -> {
                if (!party.isLeader(player.uniqueId))
                    return player.sendFixedMessage(this.plugin.config.messages.partyYouMustBeLeader)

                party.sendMessage ("&8* &cParty zostalo usuniete")
                this.plugin.partyManager.deleteParty(party)
                party.getPlayers().forEach {
                    Bukkit.getScheduler().runTaskLater(this.plugin, { this.plugin.profileManager.refreshHotbar(player) }, 2)
                }
            }
            ProfileState.SPECTATING ->
                return player.sendFixedMessage("&4Upsik! &fParty możesz tylko usunąć w lobby")
            ProfileState.QUEUING ->
                return player.sendFixedMessage("&4Upsik! &fParty możesz tylko usunąć w lobby")
        }
    }

    @EventHandler
    fun onLeave(event: PartyLeaveEvent) {
        val player = event.sender
        val party = this.plugin.partyManager.getParty(player.uniqueId)
                ?: return player.sendFixedMessage(this.plugin.config.messages.partyYouDontHaveParty)
        if (party.isLeader(player.uniqueId) && !event.quit)
            return player.sendFixedMessage(this.plugin.config.messages.partyYouAreLeader)

        if (party.leader == player.uniqueId) {
            this.plugin.server.pluginManager.callEvent(PartyDeleteEvent(player))
        } else {
            party.members.remove(player.uniqueId)
            Bukkit.getScheduler().runTaskLater(this.plugin, { this.plugin.profileManager.refreshHotbar(player) }, 2)
            party.sendMessage("&8* &4${player.name} &copuścił party")
        }
        this.plugin.partyManager.leaveParty(player.uniqueId)
    }

    @EventHandler
    fun onKick(event: PartyKickEvent) {
        val player = event.sender
        val party = this.plugin.partyManager.getParty(player.uniqueId)
                ?: return player.sendFixedMessage(this.plugin.config.messages.partyYouDontHaveParty)
        if (!party.isLeader(player.uniqueId))
            return player.sendFixedMessage(this.plugin.config.messages.partyYouMustBeLeader)

        val kickPlayer = Bukkit.getPlayer(event.kickPlayerName)
                ?: return player.sendFixedMessage(this.plugin.config.messages.playerOffline)

        if (!party.members.contains(kickPlayer.uniqueId))
            return player.sendFixedMessage(this.plugin.config.messages.partyPlayerNotInYoursParty)

        if (party.leader == kickPlayer.uniqueId) {
            return player.sendFixedMessage(this.plugin.config.messages.partyYoyAreLeader)

        } else {
            party.members.remove(kickPlayer.uniqueId)
            Bukkit.getScheduler().runTaskLater(this.plugin, { this.plugin.profileManager.refreshHotbar(player) }, 2)
            party.sendMessage("&8* &4${kickPlayer.name} &czostał wyrzucony z party")
        }
        this.plugin.partyManager.leaveParty(kickPlayer.uniqueId)
    }

    @EventHandler
    fun onJoin(event: PartyJoinEvent) {
        val player = event.sender
        if (this.plugin.partyManager.getParty(player.uniqueId) != null)
            return player.sendFixedMessage(this.plugin.config.messages.partyYouAlreadyGotParty)

        val party = this.plugin.partyManager.getParty(Bukkit.getPlayer(event.leaderName).uniqueId)
                ?: return player.sendFixedMessage(this.plugin.config.messages.partyPlayerDontHaveParty)

        if (!this.plugin.partyManager.hasInvite(party.leader, player.uniqueId))
            return player.sendFixedMessage(this.plugin.config.messages.partyYouDontHaveInvite)
        val profile = this.plugin.profileManager.getProfile(player)
        when (profile.profileState) {
            ProfileState.FIGHTING ->
                return player.sendFixedMessage("&4Upsik! &fDo party mozesz dołączyć tylko w lobby")
            ProfileState.LOADING ->
                return player.sendFixedMessage("&4Upsik! &fDo party mozesz dołączyć tylko w lobby")
            ProfileState.LOBBY -> {
                this.plugin.partyManager.joinToParty(player, party)
                party.members.add(player.uniqueId)
                Bukkit.getScheduler().runTaskLater(this.plugin, { this.plugin.profileManager.refreshHotbar(player) }, 2)
                party.sendMessage("&8* &4${player.name} &cdołączył do party")
            }
            ProfileState.SPECTATING ->
                return player.sendFixedMessage("&4Upsik! &fDo party mozesz dołączyć tylko w lobby")
            ProfileState.QUEUING ->
                return player.sendFixedMessage("&4Upsik! &fDo party mozesz dołączyć tylko w lobby")
        }
    }

    @EventHandler
    fun onInvite(event: PartyInviteEvent) {
        val player = event.sender
        val party = this.plugin.partyManager.getParty(player.uniqueId)
                ?: return player.sendFixedMessage(this.plugin.config.messages.partyYouDontHaveParty)

        if (!party.isLeader(player.uniqueId))
            return player.sendFixedMessage(this.plugin.config.messages.partyYouMustBeLeader)

        val invitedPlayer = Bukkit.getPlayer(event.invitedName)
                ?: return player.sendFixedMessage(this.plugin.config.messages.playerOffline)
        this.plugin.partyManager.inviteToParty(invitedPlayer, party)
        player.sendFixedMessage("&8* &cZaprosiłeś &4${invitedPlayer.name} &cdo party")
        invitedPlayer.sendHoverMessageCommand("&8* &eZostałeś zaproszony do party &e&lDOLĄCZ", "&7Kliknij aby dołączyć.", "/party dolacz ${event.sender.name}")
    }

}