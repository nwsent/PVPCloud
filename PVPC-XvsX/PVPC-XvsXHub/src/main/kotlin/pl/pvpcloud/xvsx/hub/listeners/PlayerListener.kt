package pl.pvpcloud.xvsx.hub.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.profile.ProfileState

class PlayerListener(private val plugin: XvsXPlugin) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        this.plugin.profileManager.getProfile(player.uniqueId)
                ?: this.plugin.profileManager.createProfile(player)
        this.plugin.profileManager.apply(player, ProfileState.LOBBY, teleport = true, connect = true)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.findProfile(player)
        when (profile.profileState) {
            ProfileState.QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.findProfile(player)
        when (profile.profileState) {
            ProfileState.QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
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

            val bh = h / 15.5
            val bw = w / 4.0
            p.velocity = location.direction.setY(bh).multiply(bw)
        }
    }
}