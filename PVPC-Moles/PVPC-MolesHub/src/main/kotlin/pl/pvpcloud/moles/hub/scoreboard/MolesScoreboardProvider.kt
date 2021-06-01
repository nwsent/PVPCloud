package pl.pvpcloud.moles.hub.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.profile.ProfileState.LOBBY
import pl.pvpcloud.moles.hub.profile.ProfileState.QUEUING
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.tools.ToolsAPI

class MolesScoreboardProvider(private val plugin: MolesPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val sb = arrayListOf<String>()
        val profile = this.plugin.profileManager.findProfile(p)
        sb.add("")
        sb.add("&7Nick&8: &f${p.name}")
        when (profile.profileState) {
            LOBBY -> {
                val user = ToolsAPI.findUserByUUID(p.uniqueId)
                sb.add("&7Stan konta&8: &f${user.coins}")
                sb.add("")
                sb.add("&7Walczy&8: &f${this.plugin.queueManager.queueThread.fightPlayers}")
                sb.add("&7Kolejka&8: &f${this.plugin.queueManager.queueThread.queuePlayers}")
            }
            QUEUING -> {
                sb.add("")
                sb.add("&7Poczekaj aż gracze")
                sb.add("&7dołaczą do poczekalni.")
                sb.add("")
                val queueWait = this.plugin.queueManager.getQueueWait(p.uniqueId)
                if (queueWait != null) {
                    val playersSize = queueWait.getPlayersSize()
                    sb.add("&7Graczy&8: &f$playersSize/16")
                    if (playersSize >= this.plugin.config.minToStart) {
                        sb.add("&7Start za&8: &a${queueWait.countdown}")
                    } else {
                        sb.add("&7Potrzeba&8: &f${this.plugin.config.minToStart - playersSize}")
                    }
                }
            }
        }
        sb.add("")
        sb.add(" &e&l* &fKrety")
        return sb
    }
}