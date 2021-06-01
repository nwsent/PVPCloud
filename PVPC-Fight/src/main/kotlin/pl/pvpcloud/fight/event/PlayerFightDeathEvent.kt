package pl.pvpcloud.fight.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import pl.pvpcloud.fight.basic.FightStats
import pl.pvpcloud.fight.enums.FightKillerType

open class PlayerFightDeathEvent(
        val player: Player,
        val killerType: FightKillerType,
        val fightStats: ArrayList<FightStats>
) : Event() {

    override fun getHandlers() = getHandlerList()

    companion object {
        private val handlers = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
}