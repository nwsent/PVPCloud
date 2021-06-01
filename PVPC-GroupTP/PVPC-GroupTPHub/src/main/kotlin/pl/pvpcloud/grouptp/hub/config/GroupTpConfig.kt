package pl.pvpcloud.grouptp.hub.config

import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.grouptp.hub.basic.Teleport
import pl.pvpcloud.grouptp.hub.basic.TeleportType

class GroupTpConfig {

    val spawnLocation = LocationHelper(0.0, 64.0, 0.0)

    val teleports = arrayListOf(
            Teleport(
                    LocationHelper(10.0, 64.0, 20.0),
                    "Start",
                    TeleportType.ALL
            )
    )
}