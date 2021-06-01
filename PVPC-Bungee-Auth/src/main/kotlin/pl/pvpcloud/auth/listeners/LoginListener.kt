package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.holders.PlayerPremiumHolder
import pl.pvpcloud.common.extensions.fixColors

class LoginListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLogin(event: LoginEvent) {
        if (event.isCancelled) return
        val name = event.connection.name
        val player = plugin.authRepository.getAuthPlayer(name)

        val hasPaid = PlayerPremiumHolder.players.getIfPresent(name)
                ?: false

        if (player == null) {
            if (hasPaid) return

            plugin.authRepository.createPlayer(name)
        } else {
            if (!hasPaid) return
            plugin.authRepository.deletePlayer(player)
            event.setCancelReason(TextComponent("&cZmieniono konto na &4PREMIUM\n &7Wejdź jeszcze raz!".fixColors()))
            event.isCancelled = true
        }
    }
}