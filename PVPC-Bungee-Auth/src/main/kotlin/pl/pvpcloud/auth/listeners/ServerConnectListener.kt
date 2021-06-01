package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.auth.AuthPlugin

class ServerConnectListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onServerConnect(event: ServerConnectEvent) {
        if (event.isCancelled) return

        val proxiedPlayer = event.player

        val player = plugin.authRepository.getAuthPlayer(proxiedPlayer.name)
                ?: return

        if (player.isLogged) {
            return
        }

        if (plugin.serversHelper.getOnlineServers() != event.target) {
            val serverInfo = plugin.serversHelper.getRandomAuthServers()
            if (serverInfo != event.target) {
                event.target = serverInfo
            }
        }
    }
}