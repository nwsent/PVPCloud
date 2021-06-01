package pl.pvpcloud.tag.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.tag.TagPlugin

class PlayerListener(private val plugin: TagPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.tagManager.createPlayer(player)
        plugin.tagManager.updatePlayer(player)
    }

    @EventHandler
    fun onQuit(event: PlayerJoinEvent) {
        val player = event.player
        plugin.tagManager.removeBoard(player)
    }
}