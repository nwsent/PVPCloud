package pl.pvpcloud.tools.tasks

import org.bukkit.Sound
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin

class RewardTimeSpentTask(private val plugin: ToolsPlugin) : BukkitRunnable() {

    override fun run() {
        this.plugin.server.onlinePlayers.forEach {
            ToolsAPI.addCoins(it.uniqueId, 2)
        }
    }

}