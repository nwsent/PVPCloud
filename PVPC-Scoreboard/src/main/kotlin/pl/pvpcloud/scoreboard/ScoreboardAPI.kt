package pl.pvpcloud.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider

object ScoreboardAPI {

    internal lateinit var plugin: ScoreboardPlugin

    fun setDefaultScoreboardProvider(scoreboardProvider: ScoreboardProvider) {
        plugin.scoreboardManager.defaultScoreboardProvider = scoreboardProvider
    }

    fun setProviderForPlayer(player: Player, scoreboardProvider: ScoreboardProvider) {
        plugin.scoreboardManager.getPlayerScoreboard(player).setProvider(scoreboardProvider)
    }

    fun setActiveScoreboardForPlayer(player: Player, active: Boolean) {
        plugin.scoreboardManager.getPlayerScoreboard(player).setActive(active)
    }
}