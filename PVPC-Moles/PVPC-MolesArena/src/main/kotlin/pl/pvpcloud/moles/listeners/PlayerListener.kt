package pl.pvpcloud.moles.listeners

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.spigotmc.event.player.PlayerSpawnLocationEvent
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.tag.event.PlayerChangeTag

class PlayerListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onSpawnLocation(event: PlayerSpawnLocationEvent) {
        val id = this.plugin.matchManager.playerMatchTeamId[event.player.uniqueId]
                ?: return
        val match = this.plugin.matchManager.getMatch(event.player)
                ?: return
        if (id == 0) {
            event.spawnLocation = match.arena.getAttackSpawn()
        } else {
            event.spawnLocation = match.arena.getDefendSpawn()
        }

    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        when (player.gameMode) {
            GameMode.SURVIVAL -> {
                this.plugin.matchManager.getMatch(player)?.handleDeath(player, true)
            }
            else -> return
        }
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        when (player.gameMode) {
            GameMode.SURVIVAL -> {
                this.plugin.matchManager.getMatch(player)?.handleDeath(player, true)
            }
            else -> return
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val damaged = event.entity
        damaged.health = 20.0
        val match = this.plugin.matchManager.getMatch(damaged.player)
                ?: throw NullPointerException("match is null :O")
        match.handleDeath(damaged, false)
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        if (event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            val match = this.plugin.matchManager.getMatch(event.player)
                    ?: throw NullPointerException("wtf match is null")
            if (match.matchState != MatchState.FIGHTING) {
                event.isCancelled = true
            }
        }
        if (event.cause == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerChangeTag(event: PlayerChangeTag) {
        event.prefix += this.plugin.matchManager.getTag(event.player, event.requester)
    }

    @EventHandler
    fun newApple(event: PlayerItemConsumeEvent) {
        val itemStack: ItemStack = event.item
        if (itemStack.type != Material.GOLDEN_APPLE) {
            return
        }
        val p: Player = event.player
        if (itemStack.durability.toInt() == 1) {
            if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                p.removePotionEffect(PotionEffectType.ABSORPTION)
            }
            if (p.hasPotionEffect(PotionEffectType.REGENERATION)) {
                p.removePotionEffect(PotionEffectType.REGENERATION)
            }
            if (p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE)
            }
            if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE)
            }
            p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 300, 5))
            p.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 1200, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3000, 0))
            p.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3000, 0))
        } else {
            if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                p.removePotionEffect(PotionEffectType.ABSORPTION)
            }
            if (p.hasPotionEffect(PotionEffectType.REGENERATION)) {
                p.removePotionEffect(PotionEffectType.REGENERATION)
            }
            p.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 140, 1))
            p.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 1200, 1))
        }
    }

}