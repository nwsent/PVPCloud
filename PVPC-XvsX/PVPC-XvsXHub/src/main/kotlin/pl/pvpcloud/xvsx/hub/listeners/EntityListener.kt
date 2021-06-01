package pl.pvpcloud.xvsx.hub.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import pl.pvpcloud.xvsx.hub.XvsXPlugin

class EntityListener(private val plugin: XvsXPlugin) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                val player = event.entity as Player
                player.fallDistance = 0.0f
                player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entity is Player) {
            event.isCancelled = true
        }
    }
}