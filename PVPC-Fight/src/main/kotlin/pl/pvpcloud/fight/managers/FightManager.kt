package pl.pvpcloud.fight.managers

import pl.pvpcloud.fight.FightPlugin
import pl.pvpcloud.fight.basic.Fight
import java.util.*
import kotlin.collections.HashMap

class FightManager(private val plugin: FightPlugin) {

    private val _fights = HashMap<UUID, Fight>()
    private val fights get() = _fights.values
    val validFights get() = fights.filter { it.lastAttack > System.currentTimeMillis() }

    fun registerFight(uniqueId: UUID) = Fight(uniqueId).also { _fights[uniqueId] = it }

    fun unregisterFight(uniqueId: UUID) = _fights.remove(uniqueId)

    fun findFight(uniqueId: UUID): Fight = _fights[uniqueId] ?: throw NullPointerException("fight is null")

    fun isFighting(fight: Fight): Boolean = fight.lastAttack > System.currentTimeMillis()
}