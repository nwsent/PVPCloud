package pl.pvpcloud.grouptp.arena.listeners

import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.*
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.grouptp.arena.match.MatchState
import pl.pvpcloud.grouptp.arena.match.event.MatchEndEvent
import pl.pvpcloud.grouptp.arena.match.event.MatchStartEvent
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.tools.ToolsAPI
import java.util.concurrent.TimeUnit

class MatchListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler
    fun onMatchStart(event: MatchStartEvent) {
        val match = event.match

        match.arena.world.difficulty = Difficulty.EASY
        match.arena.world.worldBorder.setSize(match.arena.worldBorderMinSize, TimeUnit.MINUTES.toSeconds(8))

        match.getPlayersMatch().forEach {
            it.inventory.heldItemSlot = 0
            it.spigot().collidesWithEntities = true
            it.canPickupItems = true
            it.allowFlight = false
            it.isFlying = false
            it.itemOnCursor = null
            it.fallDistance = 0.0f
            it.gameMode = GameMode.SURVIVAL
            it.inventory.clear()
            it.updateInventory()
            this.plugin.matchManager.giveItems(it)
        }
    }

    @EventHandler
    fun onMatchEnd(event: MatchEndEvent) {
        val match = event.match

        match.matchState = MatchState.ENDING

        match.getPlayersMatch().forEach {
            it.canPickupItems = false
            it.allowFlight = true
            it.isFlying = true
            it.gameMode = GameMode.SPECTATOR
            it.activePotionEffects.forEach { potionEffect -> it.removePotionEffect(potionEffect.type) }
            it.clearInventory()
            it.updateInventory()
        }

        if (event.winPlayer != null) {
            ToolsAPI.addCoins(event.winPlayer, this.plugin.config.winCoins)
        }

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersWorld().forEach {
                FightAPI.getFight(it.uniqueId).clear()
                ConnectAPI.getPlayerByUUID(it.uniqueId)?.connect("gtp_hub")
            }
            this.plugin.matchManager.deleteMatch(match)
        }, 80)
        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            this.plugin.arenaManager.unload(match.arena)
        }, 120L)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val match = this.plugin.matchManager.getMatch(player)
                ?: return
        if (!match.isState(MatchState.FIGHTING)) {
            event.isCancelled = true
            return
        }
        val block = event.block
        if (block.location.blockY > 80) {
            event.isCancelled = true
            return
        }
        val isCuboid = match.arena.isInCuboid(event.block.location)
        if (isCuboid) {
            if (event.blockPlaced.type === Material.SLIME_BLOCK) {
                event.isCancelled = true
                return
            }
            player.sendFixedMessage("&4&lUpsik! &cNie mozesz budować na terenie wrogiej gildii")
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val match = this.plugin.matchManager.getMatch(player)
                ?: return
        if (!match.isState(MatchState.FIGHTING)) {
            event.isCancelled = true
            return
        }
        val isCuboid = match.arena.isInCuboid(event.block.location)
        if (isCuboid) {
            val block = event.block
            val under = event.block.location.clone().subtract(0.0, 1.0, 0.0).block
            if (under.type == Material.ENDER_STONE) {
                if (block.type == Material.STONE) {
                    object : BukkitRunnable() {
                        override fun run() {
                            block.type = Material.STONE
                        }
                    }.runTaskLater(this.plugin, 25L)
                }
                return
            } else {
                player.sendFixedMessage("&4&lUpsik! &cNie mozesz niszczyć na terenie wrogiej gildii")
                event.isCancelled = true
                return
            }
        }
    }

}