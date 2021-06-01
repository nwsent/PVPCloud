package pl.pvpcloud.tools.listeners

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.TabCompleteEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.tools.ToolsPlugin

class TabCompleteListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTabComplete(event: TabCompleteEvent) {
        if (event.suggestions.isNotEmpty()) {
            return
        }
        val args = event.cursor.split(" ")

        val player = event.sender as ProxiedPlayer
        if (!player.hasPermission("tools.tab")) {
            event.isCancelled = true
            return
        }
        val checked = (if (args.isNotEmpty()) args[args.size - 1] else event.cursor).toLowerCase()
        for (cachedPlayer in ConnectAPI.getPlayers().values) { //todo to może lagowac ale mozna testowac
            if (cachedPlayer.nick.toLowerCase().startsWith(checked)) {
                event.suggestions.add(cachedPlayer.nick)
            }
        }
    }
}