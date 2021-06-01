package pl.pvpcloud.guild.impl.task

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.guild.api.structure.GuildRepository

class InvitesTask(private val guildRepository: GuildRepository) : BukkitRunnable() {

    override fun run() {
        this.guildRepository.guildsMap.values.forEach {
            it.invites.clear()
            it.alliesInvites.clear()
        }
    }

}