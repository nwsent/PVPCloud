package pl.pvpcloud.statistics.linker.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.api.packets.PacketStatsUpdate
import pl.pvpcloud.statistics.basic.PlayerStats

class PlayerListener(private val plugin: StatisticsPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return
        DatabaseAPI.loadBySelector<PlayerStats>(this.plugin.config.collectionName, event.uniqueId.toString(), "uuid") {
            if (it == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Stats null :O (relog)")
            } else {
                this.plugin.statsRepository.playerStatsMap[it.uuid] = it
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        val uniqueId = event.player.uniqueId
        object : BukkitRunnable() {
            override fun run() {
                val playerStats = plugin.statsRepository.getPlayerStatsByUUID(uniqueId)
                        ?: return
                playerStats.updateEntity()
                NetworkAPI.publish(*plugin.config.serversToUpdate) { PacketStatsUpdate(uniqueId, plugin.config.collectionName) }
                plugin.statsRepository.playerStatsMap.remove(uniqueId)
            }
        }.runTaskLater(this.plugin, 20)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onKick(event: PlayerKickEvent) {
        val uniqueId = event.player.uniqueId
        object : BukkitRunnable() {
            override fun run() {
                val playerStats = plugin.statsRepository.getPlayerStatsByUUID(uniqueId)
                        ?: return
                playerStats.updateEntity()
                NetworkAPI.publish(*plugin.config.serversToUpdate) { PacketStatsUpdate(uniqueId, plugin.config.collectionName) }
                plugin.statsRepository.playerStatsMap.remove(uniqueId)
            }
        }.runTaskLater(this.plugin, 20)
    }

}