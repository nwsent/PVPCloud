package pl.pvpcloud.guild.impl.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.tag.event.PlayerChangeTag

class GuildTagListener(private val guildRepository: GuildRepository, private val guildFactory: GuildFactory) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTagChange(event: PlayerChangeTag) {

        event.suffix = event.suffix + this.guildFactory.getGuildTagFor(event.player, event.requester)
    }

}