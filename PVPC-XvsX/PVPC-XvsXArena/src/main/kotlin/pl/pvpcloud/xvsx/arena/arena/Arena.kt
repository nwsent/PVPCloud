package pl.pvpcloud.xvsx.arena.arena

import pl.pvpcloud.common.helpers.LocationHelper

data class Arena(
        val name: String,
        val spawnA: LocationHelper,
        val spawnB: LocationHelper,
        val min: LocationHelper,
        val max: LocationHelper,
        val enabled: Boolean
)