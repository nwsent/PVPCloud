package pl.pvpcloud.grouptp.arena.config

import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.grouptp.arena.config.GroupTpMessages

class GroupTpConfig {

    val spawnLocation = LocationHelper(0.0, 64.0, 0.0)

    val winCoins = 20

    val messages = GroupTpMessages()
}