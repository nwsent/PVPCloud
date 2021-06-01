package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.common.extensions.fixColors
import java.util.concurrent.TimeUnit

class PostLoginListener(private val plugin: AuthPlugin) : Listener {

    @EventHandler
    fun onPostLogin(event: PostLoginEvent) {
        val proxiedPlayer = event.player

        plugin.logger.info(proxiedPlayer.name + " polaczyl sie przez Host: " + event.player.pendingConnection.virtualHost.toString())

        val player = plugin.authRepository.getAuthPlayer(proxiedPlayer.name)
                ?: return

        if (!player.name.equals(proxiedPlayer.name, ignoreCase = false)) {
            proxiedPlayer.disconnect(TextComponent("&cTwoj nick to: &4${player.name}".fixColors()))
            return
        }

        player.isLogged = false

        ProxyServer.getInstance().scheduler.schedule(plugin, {
            if (!proxiedPlayer.isConnected) {
                return@schedule
            }
            if (player.isLogged) {
                return@schedule
            }
            proxiedPlayer.disconnect(TextComponent("&cCzas na logowanie przepadl!".fixColors()))
        }, 30L, TimeUnit.SECONDS)
    }
}