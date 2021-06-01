package pl.pvpcloud.grouptp.hub.listeners

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class EntityListener(private val plugin: GroupTpPlugin) : Listener {

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
            val player = event.entity as Player
            if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
                player.healthScale = 20.0
                player.maxHealth = 20.0
                player.health = 20.0
                player.spigot().collidesWithEntities = false
                player.canPickupItems = false
                player.inventory.heldItemSlot = 0
                player.level = 0
                player.foodLevel = 20
                player.saturation = 12.8f
                player.totalExperience = 0
                player.exp = 0.0f
                player.isFlying = false
                player.allowFlight = false
                player.fireTicks = 0
                player.maximumNoDamageTicks = 20
                player.isSneaking = false
                player.isSprinting = false
                player.walkSpeed = 0.2f
                player.itemOnCursor = null
                player.fallDistance = 0.0f
                player.clearInventory()
                player.gameMode = GameMode.ADVENTURE
                player.inventory.contents = this.plugin.hotBarManager.getLayout(player)
                player.updateInventory()
                return
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