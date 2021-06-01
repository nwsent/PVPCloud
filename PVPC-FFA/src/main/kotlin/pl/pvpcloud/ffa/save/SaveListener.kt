package pl.pvpcloud.ffa.save

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class SaveListener(private val saveModule: SaveModule) : Listener {

    @EventHandler
    fun onClose(event: PlayerSaveCloseEvent) {
        Bukkit.getScheduler().runTaskLater(this.saveModule.plugin, {
            this.saveModule.plugin.arenaManager.getArenaByPlayer(event.player).handleJoin(event.player)
        }, 2)
    }

}