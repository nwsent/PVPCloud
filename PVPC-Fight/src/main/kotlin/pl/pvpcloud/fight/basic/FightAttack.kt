package pl.pvpcloud.fight.basic

import pl.pvpcloud.fight.enums.FightAttackType
import java.util.*

class FightAttack(
        val uniqueId: UUID,
        val type: FightAttackType,
        val damage: Double,
        val time: Long
)