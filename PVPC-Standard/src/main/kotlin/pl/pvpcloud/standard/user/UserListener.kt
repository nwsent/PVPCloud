package pl.pvpcloud.standard.user

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.standard.StandardPlugin

class UserListener(private val plugin: StandardPlugin) : Listener {

    @EventHandler
    fun onEvent(event: PlayerJoinEvent) {
        val player = event.player

        this.plugin.userManager.addUser(player.uniqueId)
        this.plugin.arenaManager.getSpawn(player)

        this.plugin.arenaManager.getArenas().values.forEach {
            it.getPlayers().forEach { players ->
                players.hidePlayer(player)
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = this.plugin.userManager.findUser(player.uniqueId)

        this.plugin.userManager.removeUser(user.uuid)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        val user = this.plugin.userManager.findUser(player.uniqueId)

        this.plugin.userManager.removeUser(user.uuid)
    }

}