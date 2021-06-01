package pl.pvpcloud.grouptp.hub.listeners

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.material.Button
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.basic.TeleportType
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TeleportListener(private val plugin: GroupTpPlugin) : Listener {

    private val lastInteract = HashMap<UUID, Long>()

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {

        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock.type == Material.STONE_BUTTON) {
            val player = event.player
            val location = event.clickedBlock.location.clone().add(0.0, -1.0, 0.0)
            if (location.block.type == Material.JUKEBOX) {
                if (this.canTeleport(player.uniqueId)) {
                    if (event.clickedBlock.location.distance(player.location) > 2) {
                        return
                    }

                    val teleport = plugin.teleportManager.getTeleport(location) ?: return

                    if (teleport.lastTimeUse >= System.currentTimeMillis()) return

                    when (teleport.teleportType) {
                        TeleportType.SOLO -> {
                            val players = this.getPlayersInRadiusSolo(location)
                            if (players.size < 2) return
                            teleport.sendPlayers(players)
                        }
                        TeleportType.ALL -> {
                            val players = this.getPlayersInRadiusAll(location)
                            if (players.size < 2) return
                            teleport.sendPlayers(players)
                        }
                    }
                }
                return
            }
        }
    }

    private fun getPlayersInRadiusAll(location: Location): Set<UUID> {
        val players = HashSet<UUID>()
        for (player in location.world.players) {
            if (location.distance(player.location) > 5) continue
            players.add(player.uniqueId)
        }
        return players
    }

    private fun getPlayersInRadiusSolo(location: Location): Set<UUID> {
        val players = HashSet<UUID>()
        for (player in location.world.players) {
            if (location.distance(player.location) > 5) continue
            players.add(player.uniqueId)
            if (players.size == 2) {
                return players
            }
        }
        return players
    }

    private fun canTeleport(uuid: UUID): Boolean {
        val time = this.lastInteract[uuid]
        if (time != null && System.currentTimeMillis() - time <= 0) {
            return false
        }
        this.lastInteract[uuid] = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1)
        return true
    }
}