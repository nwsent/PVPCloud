package pl.pvpcloud.connect.bungee.listener

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.connect.api.structure.PlayerService

class PlayerDisconnectListener(
        private val playerService: PlayerService
) : Listener {

    @EventHandler
    fun onPlayerDisconnect(event: PlayerDisconnectEvent) {
        this.playerService.deletePlayer(event.player.uniqueId)
    }

}