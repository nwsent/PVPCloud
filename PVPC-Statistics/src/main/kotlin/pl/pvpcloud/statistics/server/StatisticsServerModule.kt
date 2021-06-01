package pl.pvpcloud.statistics.server

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.server.adapters.StatsUpdateAdapter
import pl.pvpcloud.statistics.server.commands.AdminStatisticCommand
import pl.pvpcloud.statistics.server.commands.ResetStatsCommand
import pl.pvpcloud.statistics.server.listeners.PlayerListener
import pl.pvpcloud.statistics.server.tasks.DatabaseUpdateTask
import pl.pvpcloud.statistics.server.variable.*
import pl.pvpcloud.tablist.TablistAPI

class StatisticsServerModule(val plugin: StatisticsPlugin) {

    init {
        this.plugin.registerListeners(
                PlayerListener(this.plugin)
        )
        this.plugin.registerCommands(
                AdminStatisticCommand(this.plugin),
                ResetStatsCommand(this.plugin)
        )
        NetworkAPI.subscribe(
                StatsUpdateAdapter(this.plugin)
        )
        DatabaseUpdateTask(this.plugin)
        initTab()
    }

    private fun initTab() {
        TablistAPI.registerVariable(PointsVariable("points"))
        TablistAPI.registerVariable(KillsVariable("kills"))
        TablistAPI.registerVariable(AssistsVariable("assists"))
        TablistAPI.registerVariable(DeathsVariable("deaths"))
        TablistAPI.registerVariable(KDVariable("kd"))
        TablistAPI.registerVariable(LevelVariable("level"))
        TablistAPI.registerVariable(CoinsVariable("coins"))
        (1..17).forEach {
            TablistAPI.registerVariable(TopKillsVariable("killstop$it", it))
            TablistAPI.registerVariable(TopDeathsVariable("deathstop$it", it))
            TablistAPI.registerVariable(TopAssistsVariable("assiststop$it", it))
            TablistAPI.registerVariable(TopPointsVariable("pointstop$it", it))
            TablistAPI.registerVariable(TopLevelsVariable("levelstop$it", it))
        }
    }

}