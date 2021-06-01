package pl.pvpcloud.moles.config

import pl.pvpcloud.common.helpers.LocationHelper

class MolesConfig {

    val spawnLocation = LocationHelper(0.0, 64.0, 0.0)

    val minToStart = 2

    val winCoins = 20

    val maxPlayerInParty = hashMapOf(
        Pair("vip", 10),
        Pair("zeus", 12)
    )

    val messages = MolesMessages()
}