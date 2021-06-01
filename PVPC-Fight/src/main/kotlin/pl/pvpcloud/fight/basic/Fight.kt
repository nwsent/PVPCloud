package pl.pvpcloud.fight.basic

import pl.pvpcloud.fight.enums.FightAttackType
import java.util.*
import kotlin.collections.ArrayList

class Fight(val uniqueId: UUID) {

    private val _attacks = ArrayList<FightAttack>()
    val attacks: List<FightAttack> get() = Collections.unmodifiableList(_attacks.filter { it.time > System.currentTimeMillis() })
    val lastAttack get() = _attacks.maxBy { it.time }?.time ?: -1L

    fun attack(uniqueId: UUID, damage: Double, type: FightAttackType, time: Long) = _attacks.add(FightAttack(uniqueId, type, damage, time))

    fun clear() = _attacks.clear()

    fun size() = this.attacks.size
}