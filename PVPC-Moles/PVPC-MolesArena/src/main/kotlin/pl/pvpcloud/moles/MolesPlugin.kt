package pl.pvpcloud.moles

import pl.pvpcloud.moles.commands.MolesCommand
import pl.pvpcloud.moles.commands.SpectatorCommand
import pl.pvpcloud.moles.config.MolesArenas
import pl.pvpcloud.moles.config.MolesConfig
import pl.pvpcloud.moles.listeners.*
import pl.pvpcloud.moles.managers.*
import pl.pvpcloud.moles.scoreboard.MolesScoreboardProvider
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.moles.adapters.PrepareMatchAdapter
import pl.pvpcloud.moles.adapters.StatusAdapter
import pl.pvpcloud.moles.config.MolesKits
import pl.pvpcloud.moles.kit.KitCommand
import pl.pvpcloud.moles.kit.KitManager
import pl.pvpcloud.moles.shop.ShopModule
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.scoreboard.ScoreboardAPI

class MolesPlugin : CloudPlugin() {

    lateinit var config: MolesConfig
    lateinit var configArenas: MolesArenas
    lateinit var configKits: MolesKits

    lateinit var arenaManager: ArenaManager
    lateinit var matchManager: MatchManager
    lateinit var spectateManager: SpectateManager

    lateinit var kitManager: KitManager
    lateinit var shopModule: ShopModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, MolesConfig::class, "config")
        this.configArenas = ConfigLoader.load(this.dataFolder, MolesArenas::class, "arenas")
        this.configKits = ConfigLoader.load(this.dataFolder, MolesKits::class, "kits")

        this.arenaManager = ArenaManager(this)
        this.matchManager = MatchManager(this)
        this.spectateManager = SpectateManager(this)

        this.kitManager = KitManager(this)

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
                KitCommand(this),
                MolesCommand(this),
                SpectatorCommand(this)
        )

        this.shopModule = ShopModule(this)

        ScoreboardAPI.setDefaultScoreboardProvider(MolesScoreboardProvider(this))

        NetworkAPI.registerAdapters(
                PrepareMatchAdapter(this),
                StatusAdapter(this)
        )
    }

    override fun onDisable() {
        this.arenaManager.arenas.values.forEach {
            this.arenaManager.unload(it)
        }
    }
}