package pl.pvpcloud.moles.hub.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.hotbar.HotBarItem.*
import pl.pvpcloud.moles.hub.shop.ShopGui
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.party.gui.PartyGui

class HotBarListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item != null && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            val player = event.player
            val hotBarItem = this.plugin.hotbarManager.getHotBar(event.item)
                    ?: return
            when (hotBarItem) {
                QUEUE_JOIN ->
                    this.plugin.queueManager.addToQueue(player)
                QUEUE_LEAVE ->
                    this.plugin.queueManager.leaveFromQueue(player)
                PARTY_CREATE -> {
                    this.plugin.partyModule.partyFactory.createParty(player)
                    this.plugin.profileManager.refreshHotBar(player)
                }
                PARTY_INFO ->
                    PartyAPI.openPartyGui(player)
                SHOP ->
                    ShopGui.getInventory(this.plugin).open(player)
            }
            event.isCancelled = true
        }
    }

}