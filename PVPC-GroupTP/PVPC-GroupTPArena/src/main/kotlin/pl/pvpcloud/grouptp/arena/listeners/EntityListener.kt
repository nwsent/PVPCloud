package pl.pvpcloud.grouptp.arena.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.arena.match.MatchState

class EntityListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            val attacker = PlayerHelper.getDamager(event)
            if (attacker != null) {
                val match = this.plugin.matchManager.getMatch(attacker)
                        ?: return
                if (!match.isState(MatchState.FIGHTING)) {
                    event.isCancelled = true
                    return
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
                this.plugin.matchManager.sendToHub(player)
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