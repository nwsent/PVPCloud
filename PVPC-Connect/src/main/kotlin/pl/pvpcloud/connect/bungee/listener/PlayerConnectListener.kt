package pl.pvpcloud.connect.bungee.listener

import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.nats.NetworkAPI

class PlayerConnectListener(
        private val playerService: PlayerService
) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerConnect(event: ServerConnectedEvent) {
        val player = event.player
        val target = event.server

        this.playerService.updatePlayerServer(player.uniqueId, target.info.name)
    }

}