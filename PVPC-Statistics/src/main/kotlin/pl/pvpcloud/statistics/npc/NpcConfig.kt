package pl.pvpcloud.statistics.npc

import pl.pvpcloud.common.helpers.LocationHelper

class NpcConfig {

    val tops = hashSetOf(
            NPC(
                    "Natusiek",
                    LocationHelper("world", 19.5, 15.2, 2.5),
                    1
            ),
            NPC(
                    "Konieczo",
            LocationHelper("world", 17.5, 14.2, 2.5),
                    1
            ),
            NPC(
                    "Krzysiekigry",
                    LocationHelper("world", 21.5, 13.2, 2.5),
                    1
            )
    )

}