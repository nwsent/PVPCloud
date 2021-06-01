package pl.pvpcloud.guild.impl.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import pl.pvpcloud.guild.api.structure.GuildRepository

class GuildFightListener(private val guildRepository: GuildRepository) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerFight(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val player = event.entity

        val damagerGuild = this.guildRepository.getGuildByMember(damager.uniqueId) ?: return
        val playerGuild = this.guildRepository.getGuildByMember(player.uniqueId) ?: return

        if ((damagerGuild.leaderUUID == playerGuild.leaderUUID) && !damagerGuild.pvp) {
            event.isCancelled = true
        }
    }

}