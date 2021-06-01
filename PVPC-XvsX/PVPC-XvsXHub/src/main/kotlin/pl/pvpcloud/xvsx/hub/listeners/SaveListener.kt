package pl.pvpcloud.xvsx.hub.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.save.PlayerSaveCloseEvent

class SaveListener(private val plugin: XvsXPlugin) : Listener {

    @EventHandler
    fun onClose(event: PlayerSaveCloseEvent) {
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            this.plugin.profileManager.refreshHotBar(event.player)
        }, 2)
    }

}