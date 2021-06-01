package pl.pvpcloud.grouptp.hub.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.hotbar.HotBarItem
import pl.pvpcloud.grouptp.hub.save.SaveSelectKit
import pl.pvpcloud.grouptp.hub.shop.ShopGui

class HotBarListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item != null && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            val player = event.player
            val hotBarItem = this.plugin.hotBarManager.getHotBar(event.item)
                    ?: return
            when (hotBarItem) {
                HotBarItem.SHOP ->
                    ShopGui.getInventory(this.plugin).open(player)
                HotBarItem.SAVE_INVENTORY -> {
                    SaveSelectKit.getInventory(this.plugin).open(player)
                }
            }
            event.isCancelled = true
        }
    }

}