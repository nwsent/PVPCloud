package pl.pvpcloud.fight

import java.util.*

object FightAPI {

    lateinit var plugin: FightPlugin

    fun getFight(uniqueId: UUID) = this.plugin.fightManager.findFight(uniqueId)

    fun isFighting(uniqueId: UUID) = this.plugin.fightManager.isFighting(getFight(uniqueId))
}