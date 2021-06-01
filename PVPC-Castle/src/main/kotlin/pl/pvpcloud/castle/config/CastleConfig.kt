package pl.pvpcloud.castle.config

import pl.pvpcloud.common.helpers.LocationHelper

class CastleConfig {

    val spawnLocation = LocationHelper(0.0, 64.0, 0.0)

    val minToStart = 4

    val winCoins = 20

    val maxPlayerInParty = hashMapOf(
        Pair("vip", 10),
        Pair("zeus", 12)
    )

    val messages = CastleMessages()
}