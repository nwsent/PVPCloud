package pl.pvpcloud.xvsx.arena.listeners

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.arena.match.event.MatchEndEvent
import pl.pvpcloud.xvsx.arena.match.event.MatchNextRoundEvent
import pl.pvpcloud.xvsx.arena.match.event.MatchStartEvent

class MatchListener(private val plugin: XvsXPlugin) : Listener {

    @EventHandler
    fun onStart(event: MatchStartEvent) {
        val match = event.match

        match.getPlayersMatch().forEach {
            it.inventory.heldItemSlot = 0
            it.spigot().collidesWithEntities = true
            it.canPickupItems = true
            it.allowFlight = false
            it.isFlying = false
            it.itemOnCursor = null
            it.fallDistance = 0.0f
            it.gameMode = GameMode.ADVENTURE
            it.inventory.clear()
            it.updateInventory()
        }
    }

    @EventHandler
    fun onEnd(event: MatchEndEvent) {

    }

    @EventHandler
    fun onNextRound(event: MatchNextRoundEvent) {

    }

}