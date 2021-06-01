package pl.pvpcloud.save

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerSaveCloseEvent(
        val player: Player
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