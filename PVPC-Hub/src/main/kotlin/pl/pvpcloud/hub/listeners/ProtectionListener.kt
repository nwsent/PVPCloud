package pl.pvpcloud.hub.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerDropItemEvent

class ProtectionListener : Listener {

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        event.isCancelled = true
        event.foodLevel = 20
    }

    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onExplode(event: EntityExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (!event.player.hasPermission("hub.admin"))
            event.isCancelled = true

    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (!event.player.hasPermission("hub.admin"))
            event.isCancelled = true
    }


    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player)
            event.isCancelled = true
    }

}