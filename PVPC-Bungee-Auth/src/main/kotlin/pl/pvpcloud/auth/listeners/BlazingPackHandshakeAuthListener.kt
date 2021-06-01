package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.blazingpack.bpauth.BlazingPackHandshakeAuthEvent
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.helpers.PremiumHelper
import pl.pvpcloud.auth.holders.PlayerPremiumHolder

class BlazingPackHandshakeAuthListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler
    fun onBlazingPackHandshakeAuth(event: BlazingPackHandshakeAuthEvent) {
        if (event.isUsingBlazingPack) {
            PlayerPremiumHolder.players.put(event.initialHandler.name, event.isPremium)
            event.initialHandler.isOnlineMode = event.isPremium
        } else {
            val hasPaid = false
            PlayerPremiumHolder.players.put(event.initialHandler.name, hasPaid)
            event.initialHandler.isOnlineMode = hasPaid
        }
    }
}