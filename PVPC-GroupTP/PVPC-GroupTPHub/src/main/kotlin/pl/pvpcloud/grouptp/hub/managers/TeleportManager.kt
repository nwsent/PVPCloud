package pl.pvpcloud.grouptp.hub.managers

import org.bukkit.Location
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.basic.Teleport

class TeleportManager(private val plugin: GroupTpPlugin) {

    fun getTeleport(location: Location): Teleport? =
            this.plugin.config.teleports.singleOrNull { it.location.toBukkitLocation() == location }
}