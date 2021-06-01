package pl.pvpcloud.moles.listeners

import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.*
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.moles.match.event.MatchEndEvent
import pl.pvpcloud.moles.match.event.MatchStartEvent
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.moles.gui.preference.PreferenceKitGui
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.concurrent.TimeUnit

class MatchListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onMatchStart(event: MatchStartEvent) {
        val match = event.match

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
            StatsAPI.incrementValue(it.uniqueId, 0)
        }

        match.arena.world.difficulty = Difficulty.EASY
        match.arena.world.worldBorder.setSize(match.arena.worldBorderMinSize, TimeUnit.MINUTES.toSeconds(6))

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersMatch().forEach {
                PreferenceKitGui.getInventory(match).open(it)
            }
        }, 20)
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
            StatsAPI.incrementValue(it.uniqueId, 1)
        }

        match.sendTitle("", "&fWygrała drużyna &7-> ${if (event.winTeam.id == 0) "&catakujących" else "&9broniących"}".fixColors())

        val winNames = StringBuilder()
        event.winTeam.getAlivePlayersAll().forEach {
            winNames.append(it.name).append("&8, &a".fixColors())
            ToolsAPI.addCoins(it.uniqueId, this.plugin.config.winCoins)
        }

        match.getPlayersWorld().forEach {
            it.sendFixedMessage(arrayListOf(
                    "",
                    "     &e&lKoniec rozgrywki!",
                    " &8* &a${winNames} &fwygrywają tę gre.",
                    " &8* &7Zwycięzcy dostają&8: &a[+ ${this.plugin.config.winCoins} monet]",
                    ""
                )
            )
        }
        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersWorld().forEach {
                FightAPI.getFight(it.uniqueId).clear()
                ConnectAPI.getPlayerByUUID(it.uniqueId)?.connect("moles_hub")
            }
            this.plugin.matchManager.deleteMatch(match)
        }, 80)
        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            this.plugin.arenaManager.unload(match.arena)
        }, 120L)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val match = this.plugin.matchManager.getMatch(player)
                ?: return
        if (!match.isState(MatchState.FIGHTING)) {
            event.to = event.from
        }
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
        if (block.location.blockY > 75) {
            event.isCancelled = true
            return
        }
        if (block.type == Material.ENDER_STONE) {
            val up = block.location.clone().add(0.0, 1.0, 0.0).block
            if (!up.isEmpty) {
                player.sendFixedMessage("&cNie moze byc bloku nad!")
                event.isCancelled = true
                return
            }
            up.type = Material.STONE
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
        val block = event.block
        val under = event.block.location.clone().subtract(0.0, 1.0, 0.0).block
        if (under.type != Material.ENDER_STONE) {
            return
        }
        if (block.type == Material.STONE) {
            object : BukkitRunnable() {
                override fun run() {
                    block.type = Material.STONE
                }
            }.runTaskLater(this.plugin, 25L)
        }
    }

}