package pl.pvpcloud.castle

import pl.pvpcloud.castle.commands.CastleCommand
import pl.pvpcloud.castle.commands.SpectatorCommand
import pl.pvpcloud.castle.config.CastleArenas
import pl.pvpcloud.castle.config.CastleConfig
import pl.pvpcloud.castle.listeners.*
import pl.pvpcloud.castle.managers.*
import pl.pvpcloud.castle.scoreboard.CastleScoreboardProvider
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.save.SaveModule
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.shop.ShopModule

class CastlePlugin : CloudPlugin() {

    lateinit var config: CastleConfig
    lateinit var configArenas: CastleArenas

    lateinit var arenaManager: ArenaManager
    lateinit var hotbarManager: HotbarManager
    lateinit var matchManager: MatchManager
    lateinit var profileManager: ProfileManager
    lateinit var queueManager: QueueManager
    lateinit var spectateManager: SpectateManager

    lateinit var saveModule: SaveModule
    lateinit var shopModule: ShopModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, CastleConfig::class, "config")
        this.configArenas = ConfigLoader.load(this.dataFolder, CastleArenas::class, "arenas")

        this.arenaManager = ArenaManager(this)
        this.hotbarManager = HotbarManager(this)
        this.matchManager = MatchManager(this)
        this.profileManager = ProfileManager(this)
        this.queueManager = QueueManager(this)
        this.spectateManager = SpectateManager(this)

        this.registerListeners(
            CompassListener(this),
            EntityListener(this),
            EnvironmentListener(this),
            FightListener(this),
            HotbarListener(this),
            MatchListener(this),
            PlayerListener(this)
        )
        this.initCommands()
        this.registerCommands(
            CastleCommand(this),
            SpectatorCommand(this)
        )

        this.saveModule = SaveModule(this)
        this.shopModule = ShopModule(this)

        ScoreboardAPI.setDefaultScoreboardProvider(CastleScoreboardProvider(this))
    }

    override fun onDisable() {
        this.arenaManager.arenas.values.forEach {
            this.arenaManager.unload(it)
        }
    }
}