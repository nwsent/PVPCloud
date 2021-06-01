package pl.pvpcloud.moles.listeners

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.moles.party.event.PartyLeaveEvent
import pl.pvpcloud.moles.profile.ProfileState.*
import pl.pvpcloud.tag.event.PlayerChangeTag

class PlayerListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfileOrNull(player)
        if (profile == null) {
            this.plugin.profileManager.createProfile(player)
        } else {
            if (profile.name != player.name) {
                profile.name = player.name
            }
        }
        this.plugin.profileManager.apply(player, LOBBY, true, connect = true)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfile(player)
        when (profile.profileState) {
            FIGHTING -> this.plugin.matchManager.getMatch(profile)?.handleDeath(player, true)
            SPECTATING -> {
                FightAPI.plugin.fightManager.unregisterFight(player.uniqueId)
                this.plugin.spectateManager.removeSpectate(player)
            }
            QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
        val party = this.plugin.partyManager.getParty(player.uniqueId)
        if (party != null) {
            this.plugin.server.pluginManager.callEvent(PartyLeaveEvent(player, true))
        }
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        val profile = this.plugin.profileManager.getProfile(player)
        when (profile.profileState) {
            FIGHTING -> this.plugin.matchManager.getMatch(profile)?.handleDeath(player, true)
            SPECTATING -> {
                FightAPI.plugin.fightManager.unregisterFight(player.uniqueId)
                this.plugin.spectateManager.removeSpectate(player)
            }
            QUEUING -> this.plugin.queueManager.leaveFromQueue(player)
        }
        val party = this.plugin.partyManager.getParty(player.uniqueId)
        if (party != null) {
            this.plugin.server.pluginManager.callEvent(PartyLeaveEvent(player, true))
        }
    }

    @EventHandler
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        val profile = this.plugin.profileManager.getProfile(event.player)
        if (!profile.isState(FIGHTING)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemDamage(event: PlayerItemDamageEvent) {
        val profile = this.plugin.profileManager.getProfile(event.player)
        if (!profile.isState(FIGHTING)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val profile = this.plugin.profileManager.getProfile(event.player)
        if (!profile.isState(FIGHTING)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val damaged = event.entity
        damaged.health = 20.0
        val profile = this.plugin.profileManager.getProfile(damaged)
        if (profile.isState(FIGHTING)) {
            val match = this.plugin.matchManager.getMatch(profile)
                    ?: throw NullPointerException("match is null :O")
            match.handleDeath(damaged, false)
        }
    }

    @EventHandler
    fun onPlayerChangeTag(event: PlayerChangeTag) {
        event.prefix += this.plugin.matchManager.getTag(event.player, event.requester)
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
        if (event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            val profile = this.plugin.profileManager.getProfile(event.player)
            if (profile.isState(FIGHTING)) {
                val match = this.plugin.matchManager.getMatch(profile)
                        ?: throw NullPointerException("wtf match is null")
                if (match.matchState != MatchState.FIGHTING) {
                    event.isCancelled = true
                }
            }
        }
        if (event.cause == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            event.isCancelled = true
        }
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
        }
    }

}