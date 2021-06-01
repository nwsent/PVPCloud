package pl.pvpcloud.moles.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.moles.profile.ProfileState

class EntityListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            val damaged = event.entity as Player
            val damagedProfile = this.plugin.profileManager.getProfile(damaged)
            if (!damagedProfile.isState(ProfileState.FIGHTING)) {
                event.isCancelled = true
                return
            }
            val attacker = PlayerHelper.getDamager(event)
            if (attacker != null) {
                val attackProfile = this.plugin.profileManager.getProfile(attacker)
                if (!attackProfile.isState(ProfileState.FIGHTING)) {
                    event.isCancelled = true
                    return
                } else {
                    val match = this.plugin.matchManager.getMatch(attackProfile)
                            ?: throw NullPointerException("match is null ${attackProfile.matchId}")
                    if (!match.isState(MatchState.FIGHTING)) {
                        event.isCancelled = true
                        return
                    }
                    val damageProfile = this.plugin.profileManager.getProfile(damaged)
                    if (attackProfile.teamId == damageProfile.teamId) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            val profile = this.plugin.profileManager.getProfile(player)
            when (profile.profileState) {
                ProfileState.FIGHTING -> {
                    val match = this.plugin.matchManager.getMatch(profile)
                            ?: throw NullPointerException("wtf match is null")
                    if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                        match.handleDeath(player, false)
                        return
                    }
                    if (match.matchState != MatchState.FIGHTING) {
                        event.isCancelled = true
                    }
                }
                else -> {
                    if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                        this.plugin.profileManager.apply(player, ProfileState.LOBBY, true)
                        return
                    }
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entity is Player) {
            event.isCancelled = true
        }
    }
}