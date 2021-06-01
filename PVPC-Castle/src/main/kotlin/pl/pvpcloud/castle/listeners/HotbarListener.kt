package pl.pvpcloud.castle.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.gui.preferences.PreferencesGui
import pl.pvpcloud.castle.gui.spectator.SpectatorSelectMatchGui
import pl.pvpcloud.castle.hotbar.HotbarItem.*
import pl.pvpcloud.castle.profile.ProfileState
import pl.pvpcloud.save.PlayerSaveCloseEvent
import pl.pvpcloud.save.SaveAPI
import pl.pvpcloud.shop.ShopAPI

class HotbarListener(private val plugin: CastlePlugin) : Listener {

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
                    QUEUE_PREFERENCES ->
                        PreferencesGui.getInventory(plugin).open(player)
                    SAVE_INVENTORY ->
                        SaveAPI.openSaveGui(player)
                    SPECTATOR ->
                        SpectatorSelectMatchGui.getInventory(this.plugin).open(player)
                    SHOP ->
                        ShopAPI.openShopInventory(player)
                }
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerSaveClose(event: PlayerSaveCloseEvent) {
        val profile = this.plugin.profileManager.getProfile(event.player)
        profile.profileState = ProfileState.LOBBY

        Bukkit.getScheduler().runTaskLater(this.plugin, {
            this.plugin.profileManager.refreshHotbar(event.player)
        }, 1)
    }

}