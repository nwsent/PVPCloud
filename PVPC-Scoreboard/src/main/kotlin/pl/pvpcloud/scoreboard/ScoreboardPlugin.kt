package pl.pvpcloud.scoreboard

import org.bukkit.Bukkit
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.scoreboard.listeners.PlayerListener
import pl.pvpcloud.scoreboard.managers.ScoreboardManager

class ScoreboardPlugin : CloudPlugin() {

    lateinit var config: ScoreboardConfig

    lateinit var scoreboardManager: ScoreboardManager

    override fun onEnable() {
        this.config = ConfigLoader.load(this.dataFolder, ScoreboardConfig::class, "config")

        this.scoreboardManager = ScoreboardManager()

        this.server.scheduler.runTaskTimerAsynchronously(this, {
            this.scoreboardManager.scoreboards.values.forEach {
                try {
                    it.update()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 20, config.refresh)

        ScoreboardAPI.plugin = this

        this.registerListeners(
                PlayerListener(this)
        )
    }
}