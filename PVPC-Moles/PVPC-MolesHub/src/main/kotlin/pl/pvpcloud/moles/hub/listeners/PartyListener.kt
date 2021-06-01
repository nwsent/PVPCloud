package pl.pvpcloud.moles.hub.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.profile.ProfileState
import pl.pvpcloud.moles.hub.profile.ProfileState.*
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.party.event.PartyDeleteEvent
import pl.pvpcloud.party.event.PartyJoinEvent
import pl.pvpcloud.party.event.PartyKickEvent
import pl.pvpcloud.party.event.PartyLeaveEvent

class PartyListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onKick(event: PartyKickEvent) {
        val profile = this.plugin.profileManager.findProfile(event.sender)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(event.sender)
            LOBBY -> this.plugin.profileManager.refreshHotBar(event.sender)
        }
    }

    @EventHandler
    fun onQuit(event: PartyLeaveEvent) {
        val profile = this.plugin.profileManager.findProfile(event.sender)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(event.sender)
            LOBBY -> this.plugin.profileManager.refreshHotBar(event.sender)
        }
    }

    @EventHandler
    fun onJoin(event: PartyJoinEvent) {
        val profile = this.plugin.profileManager.findProfile(event.sender)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(event.sender)
            LOBBY -> this.plugin.profileManager.refreshHotBar(event.sender)
        }
        val party = PartyAPI.getParty(event.partyId)
                ?: return
        val profileJoin = this.plugin.profileManager.findProfile(party.uniqueIdLeader)
        when (profileJoin.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(party.leaderMember.player!!)
            LOBBY -> this.plugin.profileManager.refreshHotBar(event.sender)
        }
    }

    @EventHandler
    fun onDelete(event: PartyDeleteEvent) {
        val profile = this.plugin.profileManager.findProfile(event.sender)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(event.sender)
            LOBBY -> this.plugin.profileManager.refreshHotBar(event.sender)
        }
    }
}