package pl.pvpcloud.guild.api.event.default

import org.bukkit.event.HandlerList
import pl.pvpcloud.guild.api.event.GuildEvent
import pl.pvpcloud.guild.api.structure.Guild

class GuildCreateEvent(guild: Guild) : GuildEvent(guild) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

}