package pl.pvpcloud.ffa.limits

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerQuitEvent

class LimitsListener(private val limitsModule: LimitsModule) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        this.limitsModule.limitsManager.createDeposit(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        this.limitsModule.limitsManager.removeDeposit(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClose(e: InventoryCloseEvent) {
        this.limitsModule.limitsManager.checkInventoryPlayer(e.player as Player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerPickupItem(e: PlayerPickupItemEvent) {
        this.limitsModule.limitsManager.checkInventoryPlayer(e.player)
    }
}