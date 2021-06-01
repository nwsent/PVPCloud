package pl.pvpcloud.moles.managers

import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.Match

class SpectateManager(private val plugin: MolesPlugin) {

    fun addSpectate(player: Player) {
        player.clearInventory()
        player.allowFlight = true
        player.isFlying = true
        player.flySpeed = 0.2F
        player.spigot().collidesWithEntities = false
        player.itemOnCursor = null
        player.gameMode = GameMode.SPECTATOR
        player.health = 20.0
        player.foodLevel = 20
        player.saturation = 20.0f
        player.exhaustion = 0.0f
        player.fireTicks = 0
        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }
        player.canPickupItems = false
    }

    fun removeSpectate(player: Player) {
        val connectPlayer = ConnectAPI.getPlayerByUUID(player.uniqueId)
                ?: return
        connectPlayer.connect("moles_hub")
    }

}
