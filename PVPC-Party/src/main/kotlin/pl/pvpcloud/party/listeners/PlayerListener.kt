package pl.pvpcloud.party.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.event.PartyDisconnectEvent
import pl.pvpcloud.party.event.PartyKickEvent

class PlayerListener(private val partyModule: PartyModule) : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val party = PartyAPI.getParty(event.player.uniqueId)
                ?: return
        party.getMember(event.player.uniqueId).quitTime = System.currentTimeMillis()
        this.partyModule.plugin.server.pluginManager.callEvent(PartyDisconnectEvent(event.player, party.id))
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val party = PartyAPI.getParty(event.player.uniqueId)
                ?: return
        party.getMember(event.player.uniqueId).quitTime = System.currentTimeMillis()
        this.partyModule.plugin.server.pluginManager.callEvent(PartyDisconnectEvent(event.player, party.id))
    }

}