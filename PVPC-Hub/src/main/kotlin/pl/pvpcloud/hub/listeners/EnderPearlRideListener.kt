package pl.pvpcloud.hub.listeners

import org.bukkit.Material
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import org.spigotmc.event.entity.EntityDismountEvent

import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.hub.HubPlugin

class EnderPearlRideListener(private val plugin: HubPlugin) : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {

        val action = event.action
        val player = event.player
        if (action === Action.RIGHT_CLICK_AIR || action === Action.RIGHT_CLICK_BLOCK) {

            val item = player.itemInHand ?: return
            if (item.isSimilar(plugin.config.itemEnderPearl.toItemStack())) {

                val pearl = player.launchProjectile(EnderPearl::class.java)
                pearl.velocity = player.location.direction.multiply(1.5)
                pearl.passenger = player as Entity
                ItemsHelper.removeOneFromHand(player)
                event.isCancelled = true
            }
        }

    }

    @EventHandler
    fun onEntityDismound(event: EntityDismountEvent) {
        val entity = event.entity
        if (entity is Player) {
            if (entity.vehicle is EnderPearl && !entity.inventory.contains(Material.ENDER_PEARL)) {
                ItemsHelper.addItem(entity, plugin.config.itemEnderPearl.toItemStack())
            }
        }

    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        if (event.cause === TeleportCause.ENDER_PEARL) {
            if (!player.inventory.contains(Material.ENDER_PEARL)) {
                ItemsHelper.addItem(player, plugin.config.itemEnderPearl.toItemStack())
            }
            event.isCancelled = true
        }
    }

}