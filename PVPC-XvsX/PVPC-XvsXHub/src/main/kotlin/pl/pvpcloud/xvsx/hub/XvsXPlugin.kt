package pl.pvpcloud.xvsx.hub

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.xvsx.common.CommonModule
import pl.pvpcloud.xvsx.hub.config.XvsXConfig
import pl.pvpcloud.xvsx.hub.listeners.*
import pl.pvpcloud.xvsx.hub.managers.HotBarManager
import pl.pvpcloud.xvsx.hub.managers.ProfileManager
import pl.pvpcloud.xvsx.hub.managers.QueueManager
import pl.pvpcloud.xvsx.hub.managers.SaveManager
import pl.pvpcloud.xvsx.hub.scoreboard.XvsXScoreboardProvider

class XvsXPlugin : CloudPlugin() {

    lateinit var config: XvsXConfig

    lateinit var queueManager: QueueManager
    lateinit var profileManager: ProfileManager
    lateinit var hotBarManager: HotBarManager
    lateinit var saveManager: SaveManager

    lateinit var partyModule: PartyModule

    lateinit var commonModule: CommonModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, XvsXConfig::class, "config")

        this.queueManager = QueueManager(this)
        this.profileManager = ProfileManager(this)
        this.hotBarManager = HotBarManager(this)
        this.saveManager = SaveManager(this)

        this.initCommands()

        this.commonModule = CommonModule()

        this.partyModule = PartyModule(this)
        this.partyModule.maxMembers = 4 //todo nwm czy nie bedzie trzeba dac więcej jak chcemy roobić party split

        this.registerListeners(
                EntityListener(this),
                EnvironmentListener(this),
                FightListener(this),
                HotBarListener(this),
                PlayerListener(this),
                SaveListener(this)
        )

        ScoreboardAPI.setDefaultScoreboardProvider(XvsXScoreboardProvider(this))
    }

    override fun onDisable() {

    }
}