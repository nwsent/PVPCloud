package pl.pvpcloud.event

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.event.commands.AEventCommand
import pl.pvpcloud.event.commands.EventCommand
import pl.pvpcloud.event.commands.HostCommand
import pl.pvpcloud.event.config.EventConfig
import pl.pvpcloud.event.listeners.EventListener
import pl.pvpcloud.event.listeners.PlayerListener
import pl.pvpcloud.event.managers.EventManager
import pl.pvpcloud.event.scoreboard.EventScoreboardProvider
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.scoreboard.ScoreboardAPI

class EventPlugin : CloudPlugin() {

    lateinit var config: EventConfig

    lateinit var eventManager: EventManager

    lateinit var partyModule: PartyModule

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, EventConfig::class, "config")

        this.eventManager = EventManager(this)

        this.registerListeners(
                EventListener(this),
                PlayerListener(this)
        )

        this.initCommands()
        this.registerCommands(
                AEventCommand(this),
                EventCommand(this),
                HostCommand(this)
        )

        this.partyModule = PartyModule(this)
        this.partyModule.maxMembers = 10

        ScoreboardAPI.setDefaultScoreboardProvider(EventScoreboardProvider(this))
    }

    override fun onDisable() {

    }
}