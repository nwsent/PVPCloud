package pl.pvpcloud.moles.hub.queues

import org.bukkit.Bukkit
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.profile.ProfileState

class QueueThread(private val plugin: MolesPlugin) : Thread() {

    init {
        this.name = "PVPCloud-Queue"
        this.start()
    }

    var queuePlayers = 0
    var fightPlayers = 0

    override fun run() {
        while (true) {
            try {
                for (queueWait in this.plugin.queueManager.queuesWait.values) {
                    try {
                        queueWait.doTick()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                queuePlayers = 0
                fightPlayers = ConnectAPI.getPlayersOnServer("moles_1").size
                for (player in Bukkit.getOnlinePlayers()) {
                    if (this.plugin.profileManager.findProfile(player).isState(ProfileState.QUEUING)) {
                        queuePlayers++
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}