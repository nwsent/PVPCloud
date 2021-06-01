package pl.pvpcloud.moles

import org.bukkit.Bukkit
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.moles.commands.MolesCommand
import pl.pvpcloud.moles.commands.SpectatorCommand
import pl.pvpcloud.moles.config.MolesArenas
import pl.pvpcloud.moles.config.MolesConfig
import pl.pvpcloud.moles.listeners.*
import pl.pvpcloud.moles.managers.*
import pl.pvpcloud.moles.scoreboard.MolesScoreboardProvider
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.hub.PacketModePlayersInfo
import pl.pvpcloud.save.KitModule
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.shop.ShopModule

class MolesPlugin : CloudPlugin() {

    lateinit var config: MolesConfig
    lateinit var configArenas: MolesArenas

    lateinit var arenaManager: ArenaManager
    lateinit var hotbarManager: HotbarManager
    lateinit var matchManager: MatchManager
    lateinit var partyManager: PartyManager
    lateinit var profileManager: ProfileManager
    lateinit var queueManager: QueueManager
    lateinit var spectateManager: SpectateManager

    lateinit var kitModule: KitModule
    lateinit var shopModule: ShopModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, MolesConfig::class, "config")
        this.configArenas = ConfigLoader.load(this.dataFolder, MolesArenas::class, "arenas")

        this.arenaManager = ArenaManager(this)
        this.hotbarManager = HotbarManager(this)
        this.matchManager = MatchManager(this)
        this.partyManager = PartyManager(this)
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
                PartyListener(this),
                PlayerListener(this)
        )
        this.initCommands()
        this.registerCommands(
                MolesCommand(this),
                SpectatorCommand(this)
        )

        this.kitModule = KitModule(this)
        this.shopModule = ShopModule(this)

        ScoreboardAPI.setDefaultScoreboardProvider(MolesScoreboardProvider(this))
    }

    override fun onDisable() {
        this.arenaManager.arenas.values.forEach {
            this.arenaManager.unload(it)
        }
    }
}