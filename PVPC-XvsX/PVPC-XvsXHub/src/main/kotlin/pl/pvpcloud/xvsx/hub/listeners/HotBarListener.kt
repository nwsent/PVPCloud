package pl.pvpcloud.xvsx.hub.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.gui.KitSelectGui
import pl.pvpcloud.xvsx.hub.hotbar.HotBarItem.*
import pl.pvpcloud.xvsx.hub.save.SaveSelectKit

class HotBarListener(private val plugin: XvsXPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item != null && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            val player = event.player
            val hotBarItem = this.plugin.hotBarManager.getHotBar(event.item)
                    ?: return
            event.isCancelled = true
            when (hotBarItem) {
                QUEUE_JOIN -> KitSelectGui.getInventory(this.plugin).open(player)
                QUEUE_LEAVE -> this.plugin.queueManager.leaveFromQueue(player)
                SAVE_INVENTORY -> SaveSelectKit.getInventory(this.plugin).open(player)
                PARTY_CREATE -> {
                    this.plugin.partyModule.partyFactory.createParty(player)
                    this.plugin.profileManager.refreshHotBar(player)
                }
                PARTY_INFO ->
                    PartyAPI.openPartyGui(player)
            }
        }
    }
}