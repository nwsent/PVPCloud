package pl.pvpcloud.common.actionbar

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendActionBar

class ActionBarTask : BukkitRunnable() {

    override fun run() {
        Bukkit.getOnlinePlayers().forEach {
            val event = PlayerActionBarEvent(it, arrayOf(""))
            Bukkit.getPluginManager().callEvent(event)
            it.sendActionBar(event.lines.joinToString(separator = "\n").fixColors())
        }
    }
}