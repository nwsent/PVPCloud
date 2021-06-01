package pl.pvpcloud.moles.listeners

import org.bukkit.Bukkit
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
import pl.pvpcloud.common.ranking.RankingAlgorithm
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.moles.match.MatchTask
import pl.pvpcloud.moles.match.event.MatchEndEvent
import pl.pvpcloud.moles.match.event.MatchStartEvent
import pl.pvpcloud.moles.profile.ProfileState
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.fight.enums.FightKillerType
import pl.pvpcloud.fight.event.PlayerFightDeathEvent
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.moles.gui.preference.PreferenceKitGui
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI
import kotlin.math.roundToInt

class MatchListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onMatchStart(event: MatchStartEvent) {
        val match = event.match

        match.arena.world.difficulty = Difficulty.EASY

        val spawnDefend = match.arena.getDefendSpawn()
        val spawnAttack = match.arena.getAttackSpawn()

        match.getPlayersMatch().forEach {
            val profile = this.plugin.profileManager.getProfile(it)

            profile.matchId = match.matchId
            profile.teamId = match.getTeamId(it)
            profile.profileState = ProfileState.FIGHTING

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

        match.getPlayersAttack().forEach {
            it.teleport(spawnAttack)
        }

        match.getPlayersDefend().forEach {
            it.teleport(spawnDefend)
        }

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersMatch().forEach {
                PreferenceKitGui.getInventory(match).open(it)
            }
        }, 20)

        MatchTask(match)
    }

    @EventHandler
    fun onMatchEnd(event: MatchEndEvent) {
        val match = event.match

        match.matchState = MatchState.ENDING

        match.getPlayersMatchAlive().forEach {
            val player = it.getPlayer()
            if (player != null) {
                player.allowFlight = true
                player.isFlying = true
                player.gameMode = GameMode.ADVENTURE
                player.activePotionEffects.forEach { potionEffect -> player.removePotionEffect(potionEffect.type) }
                player.clearInventory()
                player.updateInventory()
                this.plugin.profileManager.getProfile(player).teamId = -1
                StatsAPI.incrementValue(player.uniqueId, 1)
            }
        }

        match.sendTitle("", "&fWygrała drużyna &7-> ${if (event.winTeam.teamId == 0) "&catakujących" else "&9broniących"}".fixColors())

        val winNames = StringBuilder()
        event.winTeam.getAlivePlayers().forEach {
            winNames.append(it.name).append(" ")
            ToolsAPI.addCoins(it.uniqueId, this.plugin.config.winCoins)
        }

        match.getPlayersWorld().forEach {
            this.plugin.config.messages.matchEndMessage.forEach { message ->
                it.sendFixedMessage(message.rep("%win%", winNames.toString()).rep("%x%", this.plugin.config.winCoins.toString()))
            }
        }
        this.plugin.spectateManager.removeSpectators(match)
        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersWorld().forEach {
                this.plugin.profileManager.apply(it, ProfileState.LOBBY, true)
                FightAPI.getFight(it.uniqueId).clear()
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
        val profile = this.plugin.profileManager.getProfile(player)
        if (profile.profileState == ProfileState.FIGHTING) {
            val match = this.plugin.matchManager.getMatch(player.uniqueId)
                ?: return
            if (!match.isState(MatchState.FIGHTING)) {
                event.to = event.from
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfile(player)
        if (profile.isState(ProfileState.FIGHTING)) {
            val match = this.plugin.matchManager.getMatch(profile)
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
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfile(player)
        if (profile.isState(ProfileState.FIGHTING)) {
            val match = this.plugin.matchManager.getMatch(profile)
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

}