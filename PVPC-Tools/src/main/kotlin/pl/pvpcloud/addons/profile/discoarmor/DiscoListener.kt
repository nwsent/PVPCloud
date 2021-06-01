package pl.pvpcloud.addons.profile.discoarmor

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.profile.discoarmor.DiscoType
import pl.pvpcloud.tools.ToolsAPI

class DiscoListener(private val addonsModule: AddonsModule) : Listener {

    @EventHandler
    fun openInventory(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        if (player.isSneaking && event.slot > 30) {
            event.isCancelled = true
            player.closeInventory()
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onJoin(event: PlayerJoinEvent) {
        val user = ToolsAPI.findUserByUUID(event.player.uniqueId)

        val discoType = user.settings.discoType

        if (discoType == "-") return

        this.addonsModule.discoManager.init(user.uuid, DiscoType.valueOf(discoType))
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(event: PlayerQuitEvent) {
        this.addonsModule.discoManager.removeColor(event.player.uniqueId)
    }
}