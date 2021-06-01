package pl.pvpcloud.statistics.server.tasks

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.statistics.StatisticsPlugin

class DatabaseUpdateTask(private val plugin: StatisticsPlugin) : BukkitRunnable() {

    init {
        runTaskTimerAsynchronously(this.plugin, 6000, 6000)
    }

    override fun run() {
        this.plugin.statsRepository.playerStatsMap.values.forEach {
            it.updateEntity()
        }
    }

}