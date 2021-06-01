package pl.pvpcloud.tools.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import pl.pvpcloud.tools.ToolsPlugin

class BlockingListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onDamage(event: PlayerInteractEvent) {
        val attacker = event.player
        if (attacker.isBlocking) {
            val itemStack = attacker.itemInHand
            if (itemStack != null) {
                attacker.itemInHand = null
                attacker.itemInHand = itemStack
            }
        }
    }

    @EventHandler
    fun onDamage(event: PlayerMoveEvent) {
        val attacker = event.player
        if (attacker.isBlocking) {
            val itemStack = attacker.itemInHand
            if (itemStack != null) {
                attacker.itemInHand = null
                attacker.itemInHand = itemStack
            }
        }
    }

    private val slots = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(event: PlayerDropItemEvent) {
        if (!event.isCancelled) {
            return
        }
        val attacker = event.player
        val itemStack = attacker.itemInHand
        if (itemStack != null) {
            val actualSlot = attacker.inventory.heldItemSlot
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                Bukkit.getScheduler().runTaskLater(this.plugin, {
                    attacker.inventory.heldItemSlot = actualSlot
                    attacker.updateInventory()
                }, 2)
                attacker.inventory.heldItemSlot = slots.minus(actualSlot).random()
                attacker.updateInventory()
            }, 2)
        }
    }

}