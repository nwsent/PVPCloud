package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.packets.PacketAuthPlayerUpdate
import pl.pvpcloud.nats.NetworkAPI

class DisconnectListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler
    fun onDisconnect(event: PlayerDisconnectEvent) {
        val authPlayer = plugin.authRepository.getAuthPlayer(event.player.name)
                ?: return
        authPlayer.isLogged = false
        NetworkAPI.publish { PacketAuthPlayerUpdate(authPlayer) }
    }
}