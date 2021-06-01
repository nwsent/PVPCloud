package pl.pvpcloud.tools.listeners

import net.md_5.bungee.BungeeTitle
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class PostLoginListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        BungeeTitle()
                .title(TextComponent(this.plugin.config.titleWelcome[0].fixColors()))
                .subTitle(TextComponent(this.plugin.config.titleWelcome[1].fixColors()))
                .send(player)

        plugin.config.messageWelcome.forEach { player.sendFixedMessage(it.rep("%nick%", player.name)) }
    }
}