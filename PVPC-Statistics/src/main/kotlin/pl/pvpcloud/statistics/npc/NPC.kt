package pl.pvpcloud.statistics.npc

import pl.pvpcloud.common.helpers.LocationHelper

data class NPC(
        val name: String,
        val location: LocationHelper,
        val position: Int
) {

}