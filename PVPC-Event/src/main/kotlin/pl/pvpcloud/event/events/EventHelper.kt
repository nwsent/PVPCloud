package pl.pvpcloud.event.events

import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.clearInventory

object EventHelper {

    fun pickPlayer(player: Player, event: Event) {
        player.inventory.heldItemSlot = 0
        player.spigot().collidesWithEntities = true
        player.canPickupItems = false
        player.allowFlight = false
        player.isFlying = false
        player.itemOnCursor = null
        player.fallDistance = 0.0f
        player.gameMode = GameMode.ADVENTURE
        player.clearInventory()
        player.activePotionEffects.forEach { potionEffect -> player.removePotionEffect(potionEffect.type) }
        player.health = 20.0
        player.foodLevel = 20
        player.inventory.contents = event.inventory
        player.inventory.armorContents = event.armor
        player.updateInventory()
    }

    fun specPlayer(player: Player) {
        player.inventory.heldItemSlot = 0
        player.spigot().collidesWithEntities = false
        player.canPickupItems = false
        player.allowFlight = true
        player.isFlying = false
        player.itemOnCursor = null
        player.fallDistance = 0.0f
        player.gameMode = GameMode.SPECTATOR
        player.clearInventory()
        player.openInventory.topInventory.clear()
        player.activePotionEffects.forEach { potionEffect -> player.removePotionEffect(potionEffect.type) }
        player.health = 20.0
        player.updateInventory()
    }
}