package pl.pvpcloud.tools.tasks

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerUpdate

class PlayerDataSyncTask(private val plugin: ToolsPlugin) : BukkitRunnable() {

    override fun run() {
        Bukkit.getOnlinePlayers()
                .forEach { player ->
                    this.plugin.userManager.findUserByUUID(player.uniqueId)
                            .also {
                                NetworkAPI.publish {
                                    PacketPlayerUpdate(it)
                                }
                            }
                }
    }

}