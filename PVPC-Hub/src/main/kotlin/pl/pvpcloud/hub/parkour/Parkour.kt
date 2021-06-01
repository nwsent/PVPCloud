package pl.pvpcloud.hub.parkour

import pl.pvpcloud.hub.HubPlugin
import java.util.*

class Parkour(
        val plugin: HubPlugin,
        val player: UUID
) {

    var time: Long = 0L
    var state: Int = 0

    init {
        ParkourTask(this)
    }

}