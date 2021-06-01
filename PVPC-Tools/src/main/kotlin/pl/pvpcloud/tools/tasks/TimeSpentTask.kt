package pl.pvpcloud.tools.tasks

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.tools.ToolsPlugin
import java.util.concurrent.TimeUnit

class TimeSpentTask(private val plugin: ToolsPlugin) : BukkitRunnable() {
    override fun run() {
        this.plugin.server.onlinePlayers.forEach {
            plugin.userManager.findUserByUUID(it.uniqueId).also { user ->
                user.timeSpent += TimeUnit.MINUTES.toMillis(1)
            }
        }
    }
}