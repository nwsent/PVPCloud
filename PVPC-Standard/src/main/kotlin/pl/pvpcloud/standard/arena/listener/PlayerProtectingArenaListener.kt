package pl.pvpcloud.standard.arena.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.*
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerRespawnEvent
import pl.pvpcloud.standard.StandardPlugin

class PlayerProtectingArenaListener(private val plugin: StandardPlugin) : Listener {

    @EventHandler
    fun onPlayerFall(event: EntityDamageEvent) {
        if (event.cause === DamageCause.FALL) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.respawnLocation = this.plugin.arenaManager.getSpawnLocation()
    }

    @EventHandler
    fun onDropItem(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player.hasPermission("admin.click")) return

        event.isCancelled = true
    }

}