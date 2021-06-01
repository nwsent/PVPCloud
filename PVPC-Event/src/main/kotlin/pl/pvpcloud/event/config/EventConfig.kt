package pl.pvpcloud.event.config

import pl.pvpcloud.common.helpers.LocationHelper

class EventConfig {

    var spawnLocation: LocationHelper = LocationHelper(0.0, 64.0, 0.0)

    val spawnLocationSpawnACage: LocationHelper = LocationHelper(0.0, 64.0, 0.0)
    val spawnLocationSpawnBCage: LocationHelper = LocationHelper(0.0, 64.0, 0.0)

    val spawnLocationSpawnASumo: LocationHelper = LocationHelper(125.0, 61.0, 7.0)
    val spawnLocationSpawnBSumo: LocationHelper = LocationHelper(125.0, 61.0, -6.0)

}