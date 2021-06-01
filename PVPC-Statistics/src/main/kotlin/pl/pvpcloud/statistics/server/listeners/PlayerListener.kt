package pl.pvpcloud.statistics.server.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer

class PlayerListener(private val plugin: StatisticsPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerStats = this.plugin.statsRepository.getPlayerStatsByUUID(player.uniqueId)
                ?: (this.plugin.statsRepository as StatsRepositoryServer).createPlayerStats(player.uniqueId, player.name)
        if (playerStats.name != event.player.name) {
            playerStats.name = event.player.name
            playerStats.updateEntity()
        }
    }

}