package pl.pvpcloud.moles.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.hotbar.HotbarItem.*
import pl.pvpcloud.moles.profile.ProfileState
import pl.pvpcloud.shop.ShopAPI

class HotbarListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item != null && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            val player = event.player
            val profile = this.plugin.profileManager.getProfile(player)
            if (!profile.isState(ProfileState.FIGHTING)) {
                val hotbarItem = this.plugin.hotbarManager.getHotBar(event.item)
                        ?: return
                when (hotbarItem) {
                    QUEUE_JOIN ->
                            this.plugin.queueManager.addToQueue(player)
                    QUEUE_LEAVE ->
                            this.plugin.queueManager.leaveFromQueue(player)
                    PARTY_CREATE ->
                        player.sendFixedMessage("&Tymczasowo wylaczone...")
//                        this.plugin.server.pluginManager.callEvent(PartyCreateEvent(player))
                    PARTY_INFO ->
                        player.sendFixedMessage("&Tymczasowo wylaczone...")
//                        PartyGui.getInventory(plugin).open(player)
                    SHOP ->
                        ShopAPI.openShopInventory(player)
                }
                event.isCancelled = true
            }
        }
    }


}