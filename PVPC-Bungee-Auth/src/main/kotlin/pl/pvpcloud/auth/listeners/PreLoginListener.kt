package pl.pvpcloud.auth.listeners

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.common.extensions.fixColors
import java.util.regex.Pattern

class PreLoginListener(private val plugin: AuthPlugin) : Listener {

    private val pattern = Pattern.compile("^[0-9a-zA-Z-_]+$")

    @EventHandler
    fun onPreLogin(event: PreLoginEvent) {
        if (event.isCancelled) return

        if (plugin.serversHelper.getOnlineServers().isEmpty()) {
            event.setCancelReason(TextComponent(this.plugin.config.noFreeServers.fixColors()))
            event.isCancelled = true
            return
        }

        val name = event.connection.name

        if (!pattern.matcher(name).find()) {
            event.setCancelReason(TextComponent(this.plugin.config.badCharInName.fixColors()))
            event.isCancelled = true
            return
        }
    }
}