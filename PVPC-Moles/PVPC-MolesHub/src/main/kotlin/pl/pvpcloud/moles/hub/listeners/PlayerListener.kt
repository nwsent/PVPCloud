package pl.pvpcloud.moles.hub.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.profile.ProfileState.LOBBY
import pl.pvpcloud.moles.hub.profile.ProfileState.QUEUING

class PlayerListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        this.plugin.profileManager.createProfile(player)
        this.plugin.profileManager.apply(player, LOBBY, true, connect = true)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.findProfile(player)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
        this.plugin.profileManager.removeProfile(player)
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.findProfile(player)
        when (profile.profileState) {
            QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
        this.plugin.profileManager.removeProfile(player)
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
    fun onMoveBedrock(event: PlayerMoveEvent) {
        val player = event.player
        if (player.world.name == "world" && player.location.blockY < -10) {
            player.fallDistance = 0.0f
            player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
        }
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        if (event.cause == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            event.isCancelled = true
        }
    }

}