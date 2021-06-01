package pl.pvpcloud.moles.listeners

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.moles.MolesPlugin
import java.text.DecimalFormat

class CompassListener(private val plugin: MolesPlugin) : Listener {

    private val df = DecimalFormat("#.#")

    fun getClose(player: Player): Double {
        var distance = Double.POSITIVE_INFINITY
        for (players in player.world.players) {
            if (player.uniqueId === players.uniqueId) {
                continue
            }
            val profilePlayer = this.plugin.profileManager.getProfile(player)
            val profilePlayers = this.plugin.profileManager.getProfile(players)
            if (profilePlayer.teamId == profilePlayers.teamId || profilePlayers.teamId == -1) {
                continue
            }
            val distanceTo = player.location.distance(players.location)
            if (distanceTo > distance) {
                continue
            }
            distance = distanceTo
        }
        return distance
    }

    @EventHandler
    fun onHold(event: PlayerItemHeldEvent) {
        val player = event.player
        val itemStack = player.inventory.getItem(event.newSlot)
        if (itemStack != null && itemStack.type == Material.COMPASS && !itemStack.hasItemMeta()) {
            player.sendActionBar("&cOdległość do przeciwnika: &a&l${df.format(this.getClose(player))}")
        }
    }
}