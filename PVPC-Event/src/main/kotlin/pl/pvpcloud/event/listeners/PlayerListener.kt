package pl.pvpcloud.event.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.tag.event.PlayerChangeTag

class PlayerListener(private val plugin: EventPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onJoin(event: PlayerSpawnLocationEvent) {
        event.spawnLocation = this.plugin.config.spawnLocation.toBukkitLocation()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            if (event.cause == EntityDamageEvent.DamageCause.VOID) {
                player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
                return
            }
            val pluginEvent = this.plugin.eventManager.activeEvent
            if (pluginEvent != null) {
                if (pluginEvent.name.equals("Sumo", true)) {
                    event.damage = 0.0
                    return
                }
                return
            }
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityByEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.entity is Player) {
            if (event.damager is Player) {
                val sp = PartyAPI.getParty((event.entity as Player).uniqueId) ?: return
                val sr = PartyAPI.getParty((event.damager as Player).uniqueId) ?: return
                if (sp.id == sr.id) {
                    event.damage = 0.0
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDeath(event: EntityDeathEvent) {
        event.drops.clear()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChangeTag(event: PlayerChangeTag) {
        event.prefix += this.getTag(event.player, event.requester)
    }

    private fun getTag(sender: Player, receiver: Player): String {
        val sp = PartyAPI.getParty(sender.uniqueId) ?: return ""
        val sr = PartyAPI.getParty(receiver.uniqueId) ?: return ""
        return if (sp.id != sr.id) {
            "&c"
        } else {
            "&a"
        }
    }

}