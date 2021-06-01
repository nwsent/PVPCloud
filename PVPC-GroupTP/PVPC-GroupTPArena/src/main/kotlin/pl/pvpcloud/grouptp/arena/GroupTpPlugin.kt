package pl.pvpcloud.grouptp.arena

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.grouptp.arena.adapters.PrepareMatchAdapter
import pl.pvpcloud.grouptp.arena.commands.GroupTpCommand
import pl.pvpcloud.grouptp.arena.config.GroupTpArenas
import pl.pvpcloud.grouptp.arena.config.GroupTpConfig
import pl.pvpcloud.grouptp.arena.listeners.*
import pl.pvpcloud.grouptp.arena.managers.ArenaManager
import pl.pvpcloud.grouptp.arena.managers.MatchManager
import pl.pvpcloud.grouptp.arena.scoreboard.GroupTpScoreboardProvider
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.scoreboard.ScoreboardAPI

class GroupTpPlugin : CloudPlugin() {

    lateinit var config: GroupTpConfig
    lateinit var configArenas: GroupTpArenas

    lateinit var arenaManager: ArenaManager
    lateinit var matchManager: MatchManager

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, GroupTpConfig::class, "config")
        this.configArenas = ConfigLoader.load(this.dataFolder, GroupTpArenas::class, "arenas")

        this.arenaManager = ArenaManager(this)
        this.matchManager = MatchManager(this)

        this.registerListeners(
                CompassListener(this),
                EntityListener(this),
                EnvironmentListener(this),
                FightListener(this),
                MatchListener(this),
                PlayerListener(this)
        )

        this.initCommands()
        this.registerCommands(
                GroupTpCommand(this)
        )

        ScoreboardAPI.setDefaultScoreboardProvider(GroupTpScoreboardProvider(this))

        NetworkAPI.registerAdapters(
                PrepareMatchAdapter(this)
        )
    }

    override fun onDisable() {
        this.arenaManager.arenas.values.forEach {
            this.arenaManager.unload(it)
        }
    }
}