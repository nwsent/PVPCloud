package pl.pvpcloud.grouptp.hub.listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class PlayerListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
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
    }

    @EventHandler
    fun manipulate(e: PlayerArmorStandManipulateEvent) {
        if (e.rightClicked.isVisible) e.isCancelled = true
    }

    @EventHandler
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        event.isCancelled = true
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemDamage(event: PlayerItemDamageEvent) {
        event.isCancelled = true
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val damaged = event.entity
        damaged.health = 20.0
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        if (event.cause == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMoveLaunchpad(event: PlayerMoveEvent) {
        val player = event.player
        val location = player.location
        val material = location.world.getBlockAt(location).getRelative(0, 0, 0).type
        if (material == Material.GOLD_PLATE) {
            val p = event.player
            val h = 5.0
            val w = 15.0

            val bh = h / 13
            val bw = w / 4.0
            p.velocity = location.direction.setY(bh).multiply(bw)
        }
    }

}