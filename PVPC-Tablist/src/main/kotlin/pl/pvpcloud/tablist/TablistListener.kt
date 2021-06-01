package pl.pvpcloud.tablist

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class TablistListener(private val plugin: TablistPlugin, private val manager: TablistManager) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin) {
            this.manager.createTablist(event.player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        this.manager.destroyTablist(event.player)
    }

}