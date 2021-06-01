package pl.pvpcloud.xvsx.arena

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.xvsx.arena.adapters.PrepareMatchAdapter
import pl.pvpcloud.xvsx.arena.adapters.StatusAdapter
import pl.pvpcloud.xvsx.arena.config.XvsXArenas
import pl.pvpcloud.xvsx.arena.listeners.MatchListener
import pl.pvpcloud.xvsx.arena.listeners.PlayerListener
import pl.pvpcloud.xvsx.arena.managers.ArenaManager
import pl.pvpcloud.xvsx.arena.managers.MatchManager
import pl.pvpcloud.xvsx.arena.managers.ProfileManager
import pl.pvpcloud.xvsx.arena.scoreboard.XvsXScoreboardProvider
import pl.pvpcloud.xvsx.common.CommonModule

class XvsXPlugin : CloudPlugin() {

    lateinit var configArenas: XvsXArenas

    lateinit var commonModule: CommonModule
    lateinit var arenaManager: ArenaManager
    lateinit var matchManager: MatchManager

    lateinit var profileManager: ProfileManager

    override fun onEnable() {
        this.configArenas = ConfigLoader.load(this.dataFolder, XvsXArenas::class, "arenas")

        this.commonModule = CommonModule()
        this.arenaManager = ArenaManager(this)
        this.matchManager = MatchManager(this)

        this.profileManager = ProfileManager(this)

        this.initCommands()

        this.registerListeners(
                MatchListener(this),
                PlayerListener(this)
        )

        NetworkAPI.subscribe(PrepareMatchAdapter(this))
        NetworkAPI.subscribe(StatusAdapter(this))

        ScoreboardAPI.setDefaultScoreboardProvider(XvsXScoreboardProvider(this))
    }

    override fun onDisable() {

    }
}