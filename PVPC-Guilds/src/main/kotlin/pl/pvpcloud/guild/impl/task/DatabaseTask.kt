package pl.pvpcloud.guild.impl.task

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.structure.GuildImpl

class DatabaseTask(private val guildsRepository: GuildRepository) : BukkitRunnable() {

    override fun run() {
        for (guild in this.guildsRepository.guildsMap.values) {
            if (guild is GuildImpl) {
                guild.updateEntity()
            }
        }
    }

}