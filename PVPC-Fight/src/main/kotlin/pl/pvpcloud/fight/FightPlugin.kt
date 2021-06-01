package pl.pvpcloud.fight

import org.bukkit.Bukkit
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.fight.commands.FightCommand
import pl.pvpcloud.fight.config.FightConfig
import pl.pvpcloud.fight.listeners.PlayerFightListener
import pl.pvpcloud.fight.managers.FightManager
import pl.pvpcloud.fight.tasks.FightTask

class FightPlugin : CloudPlugin() {

    lateinit var config: FightConfig

    lateinit var fightManager: FightManager

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, FightConfig::class, "config")

        this.fightManager = FightManager(this)

        this.initCommands()
        this.registerCommands(
                FightCommand(this)
        )
        this.registerListeners(
                PlayerFightListener(this)
        )

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, FightTask(this), 0, 20)

        FightAPI.plugin = this
    }

    override fun onDisable() {
        this.fightManager.validFights.forEach {
            it.clear()
        }
    }
}