package pl.pvpcloud.connect.bungee.listener

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.connect.bungee.ConnectPlugin
import pl.pvpcloud.nats.NetworkAPI

class PlayerPreConnectListener(
private val plugin: ConnectPlugin
) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerConnect(event: LoginEvent) {
        val uniqueId = event.connection.uniqueId
        if (this.plugin.playerRepository.playerMap.containsKey(uniqueId))  {
            event.setCancelReason(TextComponent("&cKtoś jest już z tym nickiem na serwerze!".fixColors()))
            event.isCancelled = true
            return
        }
        this.plugin.playerService.createPlayer(uniqueId, event.connection.name, NetworkAPI.id, "")
    }

}