package pl.pvpcloud.event.listeners

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.fight.enums.FightKillerType
import pl.pvpcloud.fight.event.PlayerFightDeathEvent
import java.text.DecimalFormat
import kotlin.math.roundToInt

class EventListener(private val plugin: EventPlugin) : Listener {

    private val df = DecimalFormat("#.##")

    @EventHandler
    fun onRespawn(event: PlayerDeathEvent) {
        val player = event.entity
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            player.spigot().respawn()
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
                player.gameMode = GameMode.SPECTATOR
            }, 3L)
        }, 5L)
    }

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.whoClicked.gameMode == GameMode.CREATIVE) {
            return
        }
        if (e.clickedInventory == null) {
            return
        }
        if (e.clickedInventory.type != InventoryType.PLAYER) {
            e.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onFightDeath(event: PlayerFightDeathEvent) {
        val victim = event.player
        victim.sendTitle("&4RIP", "&fNastępnym razem sie uda :)", 10, 50, 10)
        victim.world.strikeLightningEffect(victim.location)

        if (event.killerType == FightKillerType.OTHER) {
            Bukkit.broadcastMessage("&4${victim.name} &cumarł".fixColors())
        } else {
            val killerFightStats = event.fightStats[0]

            val killer = Bukkit.getOfflinePlayer(killerFightStats.uniqueId)
            var health = 20.0
            if (killer.player != null) {
                health = killer.player.health
            }
            Bukkit.broadcastMessage("&8* &e${victim.name} &7zabity przez&8: &e${killer.name} &8(&c${df.format(health/2)}&c❤&8)".fixColors())
        }
    }
}