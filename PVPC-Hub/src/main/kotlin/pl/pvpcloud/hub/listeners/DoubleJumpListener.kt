package pl.pvpcloud.hub.listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

import pl.pvpcloud.hub.HubPlugin

class DoubleJumpListener(private val plugin: HubPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onMoveLaunchPad(event: PlayerMoveEvent) {

        val player = event.player
        if (player.gameMode === GameMode.CREATIVE) return

        val location = player.location
        if (location.subtract(0.0, 1.0, 0.0).block.type === Material.AIR)
            player.allowFlight = true
    }

    @EventHandler
    fun onToggleFlight(event: PlayerToggleFlightEvent) {
        val player = event.player

        if (player.gameMode === GameMode.CREATIVE) return

        player.allowFlight = false
        player.isFlying = false

        player.velocity = player.location.direction.multiply(1.5)
        player.playSound(player.location, Sound.FIREWORK_BLAST, 1f, 1f)

        event.isCancelled = true
    }

}