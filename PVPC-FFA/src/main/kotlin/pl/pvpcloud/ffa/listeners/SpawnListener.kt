package pl.pvpcloud.ffa.listeners

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.*
import pl.pvpcloud.common.helpers.LocBukkitHelper
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.ffa.arena.ResetBlock
import pl.pvpcloud.fight.FightAPI

class SpawnListener(private val plugin: FFAPlugin) : Listener {

    private fun isInSpawn(player: Player, location: Location) =
            LocBukkitHelper.isInLocation(plugin.arenaManager.getArenaByPlayer(player).spawnLocation, 18, location)

    private fun isInCuboid(player: Player, location: Location) =
            LocBukkitHelper.isInLocation(plugin.arenaManager.getArenaByPlayer(player).cuboidLocation, 20, location)

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        if (!isInSpawn(player, event.itemDrop.location)) return
        if (player.hasPermission("ffa.admin")) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.type == Material.MOSSY_COBBLESTONE || event.block.type == Material.PACKED_ICE) {
            return
        }
        if (!event.player.hasPermission("ffa.admin") || event.player.gameMode != GameMode.CREATIVE) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE) return
        if (isInSpawn5(player, event.block.location) || isInCuboid(player, event.block.location)) {
            event.isCancelled = true
        } else {
            if (player.gameMode != GameMode.SURVIVAL) return
            if (event.block.type == Material.SLIME_BLOCK) {
                event.isCancelled = true
                return
            }
            plugin.arenaManager.getArenaByPlayer(event.player).blocks[event.block.location] =
                    ResetBlock(event.blockReplacedState.type, event.blockReplacedState.rawData)
        }
    }

    private fun isInSpawn5(player: Player, location: Location) =
            LocBukkitHelper.isInLocation(plugin.arenaManager.getArenaByPlayer(player).spawnLocation, 25, location)

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerBucketFill(event: PlayerBucketFillEvent) {
        if (!isInSpawn(event.player, event.blockClicked.location)) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerBucketEmpty(event: PlayerBucketEmptyEvent) {
        if (!isInSpawn(event.player, event.blockClicked.location)) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityExplode(event: EntityExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockExplode(event: BlockExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        val attacker = if (event.damager is Arrow) {
            val arrow = event.damager as Arrow
            if (arrow.shooter is Player) {
                arrow.shooter as Player
            } else return
        } else if (event.damager is Player) {
            event.damager as Player
        } else return
        if (!this.isInSpawn(player, player.location) && !this.isInSpawn(attacker, attacker.location)) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (this.isInSpawn(player, player.location)) event.isCancelled = true
    }

    @EventHandler
    fun water(event: BlockFromToEvent) {
        val id = event.block.type
        if (id === Material.WATER || id === Material.WATER_BUCKET || id === Material.STATIONARY_WATER || id === Material.LAVA || id === Material.STATIONARY_LAVA) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!FightAPI.isFighting(event.player.uniqueId)) return
        if (!isInSpawn(event.player, event.to)) return
        LocBukkitHelper.pushAwayFrom(plugin.arenaManager.getArenaByPlayer(event.player).spawnLocation, event.player, 2.0)
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerTeleportEvent) {
        if (event.cause !== PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return
        if (!FightAPI.isFighting(event.player.uniqueId)) return
        if (!isInSpawn(event.player, event.to)) return
        LocBukkitHelper.pushAwayFrom(plugin.arenaManager.getArenaByPlayer(event.player).spawnLocation, event.player, 2.0)
    }


}