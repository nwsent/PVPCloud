package pl.pvpcloud.hub.parkour

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import pl.pvpcloud.hub.HubPlugin

class ParkourListeners(private val plugin: HubPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoinToParkour(event: PlayerMoveEvent) {
        val player = event.player
        val playerLocation = player.location

        val upMaterial = playerLocation.world.getBlockAt(playerLocation).getRelative(0, 0, 0).type
        val underMaterial = playerLocation.subtract(0.0, 1.0, 0.0).block.type

        if (underMaterial === Material.QUARTZ_BLOCK && upMaterial === Material.WOOD_PLATE) {
            if (!plugin.parkourManager.parkourUser.containsKey(player.uniqueId)) {
                player.walkSpeed = 0.2f
                plugin.parkourManager.createUser(player.uniqueId)
            }
        }

    }

}
