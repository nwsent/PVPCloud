package pl.pvpcloud.hub

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.hub.commands.HubConfigCommand
import pl.pvpcloud.hub.commands.ModeCommand
import pl.pvpcloud.hub.config.HubConfig
import pl.pvpcloud.hub.listeners.ChangeServerListener
import pl.pvpcloud.hub.listeners.DoubleJumpListener
import pl.pvpcloud.hub.listeners.EnderPearlRideListener
import pl.pvpcloud.hub.listeners.ProtectionListener
import pl.pvpcloud.hub.mode.ModeManager
import pl.pvpcloud.hub.mode.ModeUpdateTask
import pl.pvpcloud.hub.parkour.*
import pl.pvpcloud.hub.scoreboard.HubScoreboardProvider
import pl.pvpcloud.hub.variable.AllPlayersOnServersVariable
import pl.pvpcloud.hub.variable.PlayersOnServerVariable
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.tablist.TablistAPI

class HubPlugin : CloudPlugin() {

    lateinit var config: HubConfig

    lateinit var modeManager: ModeManager
    lateinit var parkourManager: ParkourManager
    lateinit var parkourConfig: ParkourConfig

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, HubConfig::class, "config")
        this.parkourConfig = ConfigLoader.load(this.dataFolder, ParkourConfig::class, "parkourConfig")

        this.modeManager = ModeManager(this)
        this.parkourManager = ParkourManager(this)

        this.initCommands()
        this.registerCommands(
                ParkourCommand(this),
                HubConfigCommand(this),
                ModeCommand(this)
        )

        this.registerListeners(
                ChangeServerListener(this),
                DoubleJumpListener(this),
                EnderPearlRideListener(this),
                ParkourListeners(this),
                ProtectionListener()
        )

        ModeUpdateTask(this)
        ScoreboardAPI.setDefaultScoreboardProvider(HubScoreboardProvider())

        this.initTab()
    }

    private fun initTab() {
        this.modeManager.getModes().keys.forEach {
            TablistAPI.registerVariable(PlayersOnServerVariable(this,"players-$it"))
        }
        TablistAPI.registerVariable(AllPlayersOnServersVariable("players"))
    }

}