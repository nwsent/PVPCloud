package pl.pvpcloud.tools.tasks

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class AutoMessageTask(private val plugin: ToolsPlugin) : BukkitRunnable() {

    private var ite = plugin.autoMessageConfig.messages.iterator()

    override fun run() {
        if (plugin.autoMessageConfig.messages.isEmpty()) return
        if (!ite.hasNext()) ite = plugin.autoMessageConfig.messages.iterator()
        val messages = ite.next()

        Bukkit.getOnlinePlayers()
                .filter{
                    val user = this.plugin.userManager.getUserByUUID(it.uniqueId)
                        ?: return
                    user.settings.seeAutoMessage
                }
                .forEach {
                        it.sendFixedMessage(messages)
                }
    }

}