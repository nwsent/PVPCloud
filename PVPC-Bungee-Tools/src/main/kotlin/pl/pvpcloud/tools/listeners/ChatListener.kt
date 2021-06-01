package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class ChatListener(private val plugin: ToolsPlugin) : Listener {

    private val command = HashMap<UUID, Long>()

    private val lastMessage = ConcurrentHashMap<UUID, Long>()

    @EventHandler
    fun onChat(event: ChatEvent) {
        if (event.isCancelled) return
        val player = event.sender as ProxiedPlayer
        if (event.isProxyCommand || event.isCommand) {
            val lastCommand = command[player.uniqueId]
            if (lastCommand != null && lastCommand >= System.currentTimeMillis() && !player.hasPermission("tools.bypass.command")) {
                player.sendMessage(TextComponent(this.plugin.config.messages.youUseCommandToFast.rep("%time%", TimeUnit.MILLISECONDS.toMillis(lastCommand - System.currentTimeMillis()).toString()).fixColors()))
                event.isCancelled = true
                return
            }
            this.command[player.uniqueId] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2)
            return
        }
        if (!player.hasPermission(plugin.config.permissionChatLockedBypass)) {
            if (plugin.config.chatIsLock) {
                event.isCancelled = true
                return player.sendFixedMessage(plugin.config.messages.chatIsDisabled)
            }
        }

        if (!player.hasPermission(plugin.config.permissionChatDelayBypass)) {
            val nextMessageTime = lastMessage[player.uniqueId] ?: 0L + plugin.config.delayTimeChat
            if (nextMessageTime > System.currentTimeMillis()) {
                event.isCancelled = true
                return player.sendFixedMessage(plugin.config.messages.youGotDelayChat.rep("%time%", "${TimeUnit.MILLISECONDS.toSeconds(nextMessageTime - System.currentTimeMillis())}"))
            }
            lastMessage[player.uniqueId] = System.currentTimeMillis() + plugin.config.delayTimeChat
        }
        val mute = plugin.muteManager.getMute(player.name)
                ?: return
        player.sendFixedMessage(mute.replaceString(plugin.config.messages.youHasMute))
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerJoin(event: PostLoginEvent) {
        lastMessage[event.player.uniqueId] = System.currentTimeMillis()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerDisconnectEvent) {
        lastMessage.remove(event.player.uniqueId)
    }
}
