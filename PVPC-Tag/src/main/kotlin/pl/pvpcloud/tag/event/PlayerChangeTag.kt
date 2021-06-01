package pl.pvpcloud.tag.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerChangeTag(
        val player: Player,
        val requester: Player,
        var canSee: Boolean,
        var prefix: String,
        var suffix: String
) : Event() {
    override fun getHandlers() = getHandlerList()

    companion object {
        private val handlerList = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}