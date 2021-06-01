package pl.pvpcloud.xvsx.arena.config

import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.xvsx.arena.arena.Arena

class XvsXArenas {

    val arenas = arrayListOf(
            Arena(
                    "Las",
                    LocationHelper(15.0, 65.0, 52.0),
                    LocationHelper(15.0, 65.0, 78.0),
                    LocationHelper(0.0, 60.0, 50.0),
                    LocationHelper(30.0, 74.0, 80.0),
                    true
            )
    )
}