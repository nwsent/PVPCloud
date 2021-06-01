package pl.pvpcloud.hub.listeners

import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.hub.HubPlugin
import pl.pvpcloud.hub.gui.ModeGui

class ChangeServerListener(private val plugin: HubPlugin) : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.walkSpeed = 0.4f

        player.gameMode = GameMode.ADVENTURE
        player.inventory.heldItemSlot = 4

        player.inventory.setItem(0, plugin.config.itemEnderPearl.toItemStack())
        player.inventory.setItem(4, plugin.config.itemSelectMode.toItemStack())
        player.inventory.setItem(8, AddonsAPI.HEAD(player.name))

        player.teleport(plugin.config.lobbyLocation.toBukkitLocation())
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        if (player.location.y < -30.0F)
            player.teleport(plugin.config.lobbyLocation.toBukkitLocation())
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
            val player = event.player
            val item = player.itemInHand ?: return
            if (item.isSimilar(plugin.config.itemSelectMode.toItemStack())) {
                ModeGui.getInventory(this.plugin).open(player)
                player.playSound(player.location, Sound.CHEST_OPEN, 1f, 1f)
            }

    }

}