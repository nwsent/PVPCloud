package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.event.PlayerChatEvent
import pl.pvpcloud.tools.packets.PacketPlayerChat
import java.util.regex.Pattern

class PlayerChatListener(private val plugin: ToolsPlugin) : Listener {

    private val cloud = "PVPCloud"

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled) return

        event.isCancelled = true

        val sender = event.player
        var message = event.message

        val patternIP = ".*([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]).*".toRegex()
        if (message.matches(patternIP))
            return sender.sendFixedMessage("&4Ups! &fPodawanie kogoś ip jest zabronione.")

        /*
        val patternUrl = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?\u200C\u200B[\\w\\?\u200C\u200B=]*)?".toRegex()
        if (!message.matches(patternUrl))
            return sender.sendFixedMessage("&4Ups! &fSory, ale możesz tylko wysyłać linki od yt.")
         */

        if (sender.hasPermission("chat.bypass.color"))
            message = message.fixColors()

        val format = this.plugin.config.customFormats.getOrDefault(PlayerHelper.getGroup(sender.uniqueId), this.plugin.config.formatChat)
        val user = this.plugin.userManager.findUserByUUID(sender.uniqueId)

        val replacedFormat = format
                .rep("%nickname%", "${sender.name}${ChatColor.RESET}")
                .rep("%colorName%", ChatColor.valueOf(user.settings.chatNameColorType).toString())

        if (message.startsWith("#") && sender.hasPermission("chat.bypass.sendAll")) {
            return NetworkAPI.publish { PacketGlobalMessage((replacedFormat + message.rep("#", "")).fixColors(), "-") }
        } else {
            NetworkAPI.publish { PacketPlayerChat(sender.name, "${ChatColor.RESET} ${event.message}") }
        }
        event.recipients.map {
            PlayerChatEvent(sender, it, replacedFormat)
        }.forEach {
            Bukkit.getPluginManager().callEvent(it)
            it.receiver.sendMessage(it.format.fixColors() + message
                    .replace("marchew", cloud, true)
                    .replace("pvpstar", cloud, true)
                    .replace("bonsko", cloud, true)
                    .replace("<3", "\u2764"))
        }
    }

}
