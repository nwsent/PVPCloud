package pl.pvpcloud.statistics

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.statistics.api.PluginType.LINKER
import pl.pvpcloud.statistics.api.PluginType.SERVER
import pl.pvpcloud.statistics.api.StatsRepository
import pl.pvpcloud.statistics.commands.StatisticCommand
import pl.pvpcloud.statistics.config.StatisticsConfig
import pl.pvpcloud.statistics.linker.StatisticsLinkerModule
import pl.pvpcloud.statistics.linker.repository.StatsRepositoryLinker
import pl.pvpcloud.statistics.listeners.PlayerFightDeathListener
import pl.pvpcloud.statistics.npc.NpcConfig
import pl.pvpcloud.statistics.npc.NpcListener
import pl.pvpcloud.statistics.npc.NpcManager
import pl.pvpcloud.statistics.npc.NpcTask
import pl.pvpcloud.statistics.server.StatisticsServerModule
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer

class StatisticsPlugin : CloudPlugin() {

    lateinit var config: StatisticsConfig
    lateinit var npcConfig: NpcConfig

    lateinit var npcManager: NpcManager

    lateinit var statsRepository: StatsRepository

    override fun onEnable() {

        this.config = ConfigLoader.load(this.dataFolder, StatisticsConfig::class, "config")
        this.npcConfig = ConfigLoader.load(this.dataFolder, NpcConfig::class, "npc")

        this.initCommands()

        when (this.config.pluginTyp) {
            SERVER -> {
                this.logger.info("Load Server Statistics")
                StatisticsServerModule(this)
                this.statsRepository = StatsRepositoryServer(this)
                this.npcManager = NpcManager(this)
                this.server.scheduler.runTaskTimerAsynchronously(this, {
                    (this.statsRepository as StatsRepositoryServer).sortRankings()
                }, 10, 300)
            }
            LINKER -> {
                this.logger.info("Load Linker Statistics")
                StatisticsLinkerModule(this)
                this.statsRepository = StatsRepositoryLinker(this)
            }
        }

        this.config.customStats.forEach {
            this.statsRepository.registerCustomStats(it.key, it.value)
        }

        this.registerCommands(
                StatisticCommand(this)
        )
        this.registerListeners(
            PlayerFightDeathListener(this),
                    NpcListener(this)
        )
        NpcTask(this)

        StatsAPI.plugin = this
    }

    override fun onDisable() {
        this.statsRepository.playerStatsMap.values.forEach {
            it.updateEntity()
        }
    }

}