package pl.pvpcloud.fight.basic

import java.util.*

data class FightStats(
        val uniqueId: UUID,
        var totalDamageGiven: Double,
        var percentage: Double
)