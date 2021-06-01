package pl.pvpcloud.addons.profile

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.connect.api.ConnectAPI

class ProfileListener(private val addonsModule: AddonsModule) : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player
            if (item.type == Material.SKULL_ITEM) {
                event.isCancelled = true
                ProfileGui.getInventory(this.addonsModule).open(player)
                return
            }
            if (item.isSimilar(AddonsAPI.HUB)) {
                val user = ConnectAPI.getPlayerByUUID(player.uniqueId)
                        ?: return
                user.connect("lobby")
            }
        }
    }

}


