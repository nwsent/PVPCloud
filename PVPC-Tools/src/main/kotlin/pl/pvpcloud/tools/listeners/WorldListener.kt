package pl.pvpcloud.tools.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.tools.ToolsPlugin

class WorldListener(private val plugin: ToolsPlugin) : Listener {

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        val border = player.world.worldBorder

        if (border == null || event.cause !== PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return

        val location = event.to
        val size = border.size / 2.0
        val x = location.x - border.center.blockX
        val z = location.z - border.center.blockZ
        if ((x > size || (-x) > size) || (z > size || (-z) > size)) {
            event.isCancelled = true
            ItemsHelper.addItem(player, ItemStack(Material.ENDER_PEARL))
        }
    }

    @EventHandler
    fun weather(event: WeatherChangeEvent) {
        if (event.toWeatherState())
            event.isCancelled = true

    }

}