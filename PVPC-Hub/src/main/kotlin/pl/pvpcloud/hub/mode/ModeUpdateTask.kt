package pl.pvpcloud.hub.mode

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.hub.HubPlugin

class ModeUpdateTask(private val plugin: HubPlugin) : BukkitRunnable() {

    init {
        runTaskTimer(this.plugin, 40, 40)
    }

    override fun run() {
        this.plugin.modeManager.getModes().values.forEach {
            it.online = ConnectAPI.getPlayersOnServers(it.servers).size
        }
    }
}