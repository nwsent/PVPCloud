package pl.pvpcloud.xvsx.arena.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.xvsx.arena.XvsXPlugin

class PlayerListener(private val plugin: XvsXPlugin) : Listener {
    
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
    }
}