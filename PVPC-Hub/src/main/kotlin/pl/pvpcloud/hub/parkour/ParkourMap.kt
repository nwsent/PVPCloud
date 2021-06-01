package pl.pvpcloud.hub.parkour

import pl.pvpcloud.common.helpers.LocationHelper

data class ParkourMap(
        val id: String,
        val blocks: HashSet<Pair<Int, LocationHelper>>
)