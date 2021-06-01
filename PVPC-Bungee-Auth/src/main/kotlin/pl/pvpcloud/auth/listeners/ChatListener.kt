package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.auth.AuthPlugin

class ChatListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: ChatEvent) {
        val player = event.sender as ProxiedPlayer
        val authPlayer = plugin.authRepository.getAuthPlayer(player.name)
        if (authPlayer != null) {
            if (!authPlayer.isLogged || !authPlayer.isRegistered()) {
                val msg = event.message.toLowerCase()
                if (msg.startsWith("/l") || msg.startsWith("/login") || msg.startsWith("/reg") || msg.startsWith("/register")) {
                    return
                }
                event.isCancelled = true
            }
        }
    }
}