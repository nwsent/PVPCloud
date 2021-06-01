package pl.pvpcloud.moles.queues

import org.bukkit.Bukkit
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.profile.ProfileState

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
                fightPlayers = 0
                for (player in Bukkit.getOnlinePlayers()) {
                    when(this.plugin.profileManager.getProfile(player).profileState) {
                        ProfileState.QUEUING -> {
                            queuePlayers++
                        }
                        ProfileState.FIGHTING -> {
                            fightPlayers++
                        }
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