 package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.scoreboard.ScoreboardAPI
import pl.pvpcloud.tag.event.PlayerChangeTag
import pl.pvpcloud.tools.ToolsPlugin

class PlayerListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player

        val user = plugin.userManager.getUserByUUID(player.uniqueId)
                ?: return player.kickPlayer("&4Ups! &cZaloguj sie jeszcze raz!".fixColors())

        user.lastJoinTime = System.currentTimeMillis()

        ScoreboardAPI.setActiveScoreboardForPlayer(player, user.settings.seeScoreboard)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage = null

        val user = plugin.userManager.getUserByUUID(event.player.uniqueId)

        if (user != null)
            return user.updateEntity()

    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        event.leaveMessage = null

        val user = plugin.userManager.getUserByUUID(event.player.uniqueId)

        if (user != null)
            return user.updateEntity()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onTagRefresh(event: PlayerChangeTag) {
        val user = plugin.userManager.getUserByUUID(event.player.uniqueId)
                ?: return
        event.prefix += "${this.plugin.config.prefixOnTag.getOrDefault(PlayerHelper.getGroup(event.player.uniqueId), "")} ${if (event.player.hasPermission("addons.tagcolor")) ChatColor.valueOf(user.settings.tagColorType) else ""}"
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        if (!event.isCancelled) {
            val command = event.message.split(" ")[0]
            val helpTopic = Bukkit.getServer().helpMap.getHelpTopic(command)
            if (helpTopic == null) {
                player.sendFixedMessage("&4Ups! &fZobacz &4/pomoc &fbo nie ma takiej komendy.")
                event.isCancelled = true
            }
        }
    }

}