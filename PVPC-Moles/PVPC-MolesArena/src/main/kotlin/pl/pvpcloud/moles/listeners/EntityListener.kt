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

class EntityListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            val damaged = event.entity as Player
            val attacker = PlayerHelper.getDamager(event)
            if (attacker != null) {
                val match = this.plugin.matchManager.getMatch(attacker)
                        ?: return
                if (!match.isState(MatchState.FIGHTING)) {
                    event.isCancelled = true
                    return
                }
                val damageId = this.plugin.matchManager.playerMatchTeamId[damaged.uniqueId]
                val attackId = this.plugin.matchManager.playerMatchTeamId[attacker.uniqueId]
                if (damageId == attackId) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            val match = this.plugin.matchManager.getMatch(player)
            if (match == null) {
                this.plugin.matchManager.sendToHubMoles(player)
            } else {
                if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                    match.handleDeath(player, false)
                    return
                }
                if (match.matchState != MatchState.FIGHTING) {
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