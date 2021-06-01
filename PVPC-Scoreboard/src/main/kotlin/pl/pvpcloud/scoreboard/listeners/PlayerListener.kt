package pl.pvpcloud.scoreboard.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.scoreboard.ScoreboardPlugin

class PlayerListener(private val plugin: ScoreboardPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        this.plugin.scoreboardManager.createPlayerScoreboard(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        this.plugin.scoreboardManager.removeScoreboard(event.player)
    }
}