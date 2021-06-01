package pl.pvpcloud.guild.api.event

import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import pl.pvpcloud.guild.api.structure.Guild

abstract class GuildEvent(val guild: Guild) : Event(false), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

}