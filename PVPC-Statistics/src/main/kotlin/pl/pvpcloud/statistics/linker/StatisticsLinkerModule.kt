package pl.pvpcloud.statistics.linker

import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.linker.listeners.PlayerListener

class StatisticsLinkerModule(private val plugin: StatisticsPlugin) {

    init {
        this.plugin.registerListeners(
                PlayerListener(this.plugin)
        )
    }
}