package pl.pvpcloud.castle.match.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import pl.pvpcloud.castle.match.Match

open class MatchEvent(
        val match: Match
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