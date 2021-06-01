package pl.pvpcloud.grouptp.hub.save

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class SaveListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler
    fun onClose(event: PlayerSaveCloseEvent) {
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            event.player.inventory.contents = this.plugin.hotBarManager.getLayout(event.player)
            event.player.updateInventory()
        }, 2)
    }

}