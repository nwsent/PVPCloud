package pl.pvpcloud.common.actionbar

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerActionBarEvent(
    val player: Player,
    val lines: Array<String>
) : Event(true) {

    override fun getHandlers() = getHandlerList()

    companion object {
        private val handlerList = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}