package pl.pvpcloud.castle.listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.gui.preferences.PreferenceKitGui
import pl.pvpcloud.castle.match.MatchState
import pl.pvpcloud.castle.match.MatchTask
import pl.pvpcloud.castle.match.event.MatchEndEvent
import pl.pvpcloud.castle.match.event.MatchStartEvent
import pl.pvpcloud.castle.profile.ProfileState
import pl.pvpcloud.common.extensions.*
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.concurrent.TimeUnit

class MatchListener(private val plugin: CastlePlugin) : Listener {

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

        val spawnDefend = match.arena.getDefendSpawn()
        val spawnAttack = match.arena.getAttackSpawn()

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersAttackAlive().forEach {
                val profile = this.plugin.profileManager.getProfile(it)

                profile.matchId = match.matchId
                profile.teamId = 0
                profile.profileState = ProfileState.FIGHTING
                it.teleport(spawnAttack)
            }

            match.getPlayersDefendAlive().forEach {
                val profile = this.plugin.profileManager.getProfile(it)

                profile.matchId = match.matchId
                profile.teamId = 1
                profile.profileState = ProfileState.FIGHTING
                it.teleport(spawnDefend)
            }
        }, 10)

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            match.getPlayersMatch().forEach {
                PreferenceKitGui.getInventory(match).open(it)
            }
        }, 30)

        MatchTask(match)
    }

    @EventHandler
    fun onMatchEnd(event: MatchEndEvent) {
        val match = event.match

        match.matchState = MatchState.ENDING

        match.getPlayersMatch().forEach {
            it.allowFlight = true
            it.isFlying = true
            it.gameMode = GameMode.ADVENTURE
            it.activePotionEffects.forEach { potionEffect -> it.removePotionEffect(potionEffect.type) }
            it.clearInventory()
            it.updateInventory()
            this.plugin.profileManager.getProfile(it).teamId = -1
            StatsAPI.incrementValue(it.uniqueId, 1)
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
                FightAPI.getFight(it.uniqueId)?.clear()
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
        if (profile.profileState === ProfileState.FIGHTING) {
            val match = this.plugin.matchManager.getMatch(player.uniqueId)
                ?: return
            if (!match.isState(MatchState.FIGHTING)) {
                event.to = event.from
            }
        }
    }

    @EventHandler
    fun onExplode(event: EntityExplodeEvent) {
        val arena = this.plugin.arenaManager.getArenaByWorld(event.location.world.name)
                ?: return
        val match = this.plugin.matchManager.getMatchByArena(arena)
                ?: return
        var inCuboid = arena.isInCuboid(event.entity.location)
        if (!inCuboid) {
            for (it in event.blockList()) {
                if (arena.isInCuboid(it.location)) {
                    inCuboid = true
                    break
                }
            }
        }
        if (!inCuboid) return
        val last = match.lastExplode
        match.lastExplode = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)
        if (last < System.currentTimeMillis()) {
            match.defendTeam.sendMessage("&4* &cNa terenie gildii wybuchlo tnt! Budowac mozesz dopiero za &4${TimeUnit.MILLISECONDS.toSeconds(match.lastExplode - System.currentTimeMillis())}")
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfile(player)
        if (profile.isState(ProfileState.FIGHTING)) {
            val match = this.plugin.matchManager.getMatch(profile)
                    ?: return
            if (player.world.uid != match.arena.world.uid) {
                player.teleport(match.arena.getAttackSpawn())
            }
            if (!match.isState(MatchState.FIGHTING)) {
                event.isCancelled = true
                return
            }
            val isCuboid = match.arena.isInCuboid(event.block.location)
            if (isCuboid) {
                if (event.blockPlaced.type === Material.SLIME_BLOCK) {
                    event.isCancelled = true
                    return
                }
                if (profile.teamId == 1) {
                    if (match.arena.isInCenter(event.block.location, 3, 1, 5)) {
                        event.isCancelled = true
                    }
                    if (match.lastExplode > System.currentTimeMillis()) {
                        player.sendFixedMessage("&4* &cNa terenie gildii wybuchlo tnt! Budowac mozesz dopiero za &4${TimeUnit.MILLISECONDS.toSeconds(match.lastExplode - System.currentTimeMillis())}")
                        event.isCancelled = true
                        //event.player.damage(0.0)
                        return
                    }
                    val block = event.block
                    if (block.type == Material.ENDER_STONE) {
                        val up = block.location.clone().add(0.0, 1.0, 0.0).block
                        if (!up.isEmpty) {
                            player.sendFixedMessage("&cNie moze byc bloku nad!")
                            event.isCancelled = true
                            return
                        }
                        up.type = Material.STONE
                    }
                } else if (profile.teamId == 0) {
                    if (event.blockPlaced.type == Material.TNT) {
                        if (match.lastPlaceTnt > System.currentTimeMillis()) {
                            player.sendFixedMessage("&cTnt możesz położyć za &4${TimeUnit.MILLISECONDS.toSeconds(match.lastPlaceTnt - System.currentTimeMillis())}&csek")
                            event.isCancelled = true
                            return
                        } else {
                            match.lastPlaceTnt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)
                            return
                        }
                    }
                    player.sendFixedMessage("&4&lUpsik! &cNie mozesz budować na terenie wrogiej gildii")
                    event.isCancelled = true
                }
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
            val isCuboid = match.arena.isInCuboid(event.block.location)
            if (isCuboid) {
                if (profile.teamId == 0) {
                    player.sendFixedMessage("&4&lUpsik! &cNie mozesz niszczyć na terenie wrogiej gildii")
                    event.isCancelled = true
                } else if (profile.teamId == 1) {
                    if (match.arena.isInCenter(event.block.location, 3, 1, 5)) {
                        event.isCancelled = true
                        return
                    }
                    val block = event.block
                    val under = event.block.location.clone().subtract(0.0, 1.0, 0.0).block
                    if (under.type != Material.ENDER_STONE) {
                        return
                    }
                    if (block.type === Material.STONE) {
                        object : BukkitRunnable() {
                            override fun run() {
                                if (match.lastExplode > System.currentTimeMillis()) {
                                    return
                                }
                                block.type = Material.STONE
                            }
                        }.runTaskLater(this.plugin, 25L)
                    }
                }
            }
        }
    }

}