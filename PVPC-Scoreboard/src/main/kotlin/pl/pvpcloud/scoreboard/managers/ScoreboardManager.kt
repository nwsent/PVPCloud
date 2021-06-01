package pl.pvpcloud.scoreboard.managers

import org.bukkit.entity.Player
import pl.pvpcloud.scoreboard.basic.DefaultScoreboardProvider
import pl.pvpcloud.scoreboard.basic.PlayerScoreboard
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ScoreboardManager {

    val scoreboards: ConcurrentHashMap<UUID, PlayerScoreboard> = ConcurrentHashMap()

    @Volatile
    var defaultScoreboardProvider: ScoreboardProvider = DefaultScoreboardProvider()

    fun getPlayerScoreboard(player: Player): PlayerScoreboard {
        return scoreboards.getOrDefault(player.uniqueId, createPlayerScoreboard(player))
    }

    fun removeScoreboard(player: Player) {
        val scoreboardPlayer = getPlayerScoreboard(player)
        scoreboards.remove(player.uniqueId)
        scoreboardPlayer.disappear()
    }

    fun createPlayerScoreboard(player: Player): PlayerScoreboard {
        val playerScoreboard = PlayerScoreboard(player, defaultScoreboardProvider)
        scoreboards[player.uniqueId] = playerScoreboard
        return playerScoreboard
    }
}