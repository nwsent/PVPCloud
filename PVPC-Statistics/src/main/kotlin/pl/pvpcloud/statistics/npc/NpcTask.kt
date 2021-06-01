package pl.pvpcloud.statistics.npc

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.statistics.StatisticsPlugin

class NpcTask(private val plugin: StatisticsPlugin) : BukkitRunnable() {

    init {
        runTaskTimer(this.plugin, 240, 240)
    }

    override fun run() {

        Bukkit.getOnlinePlayers().forEach {
            this.plugin.npcManager.spawnNPC(it)
        }

    }

}