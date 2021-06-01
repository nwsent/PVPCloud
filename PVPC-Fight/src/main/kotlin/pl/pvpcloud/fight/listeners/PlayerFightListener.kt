package pl.pvpcloud.fight.listeners

import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.fight.FightPlugin
import pl.pvpcloud.fight.basic.FightStats
import pl.pvpcloud.fight.enums.FightAttackType
import pl.pvpcloud.fight.enums.FightKillerType
import pl.pvpcloud.fight.event.PlayerFightDeathEvent
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PlayerFightListener(private val plugin: FightPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val player = e.entity as? Player ?: return
        val bowAttack: Boolean
        val attacker = if (e.damager is Arrow) {
            val arrow = e.damager as Arrow
            if (arrow.shooter is Player) {
                bowAttack = true
                arrow.shooter as Player
            } else return
        } else if (e.damager is Player) {
            bowAttack = false
            e.damager as Player
        } else return
        val fight = plugin.fightManager.findFight(player.uniqueId)
        if (!plugin.fightManager.isFighting(fight)) {
            fight.clear()
        }
        fight.attack(attacker.uniqueId, e.damage, if (bowAttack) FightAttackType.BOW else FightAttackType.WEAPON, System.currentTimeMillis() + plugin.config.validAttackerTime)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val fight = this.plugin.fightManager.findFight(e.entity.uniqueId)
        if (fight.attacks.isEmpty()) {
            val event = PlayerFightDeathEvent(
                    e.entity,
                    FightKillerType.OTHER,
                    arrayListOf()
            )
            this.plugin.server.pluginManager.callEvent(event)
        } else {
            val parts = HashMap<UUID, Double>()
            fight.attacks.forEach {
                val id = it.uniqueId
                parts[id] = fight.attacks.filter { it.uniqueId == id }.sumByDouble { it.damage }
            }
            val totalDamage = fight.attacks.sumByDouble { it.damage }.toInt()
            val fightStats = HashMap<UUID, FightStats>()
            parts.forEach {
                val per = 100 * it.value / totalDamage
                if (per > this.plugin.config.minPercentToValidAssist)
                    fightStats[it.key] = FightStats(it.key, it.value, per)
            }

            val event = PlayerFightDeathEvent(
                    e.entity,
                    FightKillerType.PLAYER,
                    ArrayList(fightStats.values.sortedByDescending { it.totalDamageGiven })
            )
            this.plugin.server.pluginManager.callEvent(event)
        }

        plugin.fightManager.findFight(e.entity.uniqueId).clear()
        e.deathMessage = null
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        plugin.fightManager.registerFight(e.player.uniqueId)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val player = e.player
        val fight = plugin.fightManager.findFight(player.uniqueId)
        if (plugin.fightManager.isFighting(fight)) {
            player.health = 0.0
            Bukkit.broadcastMessage(plugin.config.messages.playerLoggedWhileFight.rep("%name%", player.name).fixColors())
        }
        plugin.fightManager.unregisterFight(player.uniqueId)
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        val player = e.player
        val fight = plugin.fightManager.findFight(player.uniqueId)
        if (!plugin.fightManager.isFighting(fight)) return
        if (player.hasPermission(plugin.config.permissionCombatDisabledBypass)) return
        val command = e.message.rep("/", "")
        plugin.config.messages.allowedCommandsWhileCombat.forEach {
            if (command.startsWith(it)) return
        }
        player.sendFixedMessage(plugin.config.messages.commandsWhileCombatAreDisabled)
        e.isCancelled = true
    }
}