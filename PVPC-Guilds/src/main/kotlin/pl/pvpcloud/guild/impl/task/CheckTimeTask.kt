package pl.pvpcloud.guild.impl.task

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository

class CheckTimeTask(private val guildRepository: GuildRepository, private val guildFactory: GuildFactory) : BukkitRunnable() {

    override fun run() {
        this.guildRepository.guildsMap.values.forEach {
            if (it.timeGuild < System.currentTimeMillis()) {
                this.guildFactory.timeOutGuild(it)
            }
        }
    }

}