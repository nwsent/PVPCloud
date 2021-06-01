package pl.pvpcloud.moles.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.moles.profile.ProfileState.*
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.tools.ToolsAPI

class MolesScoreboardProvider(private val plugin: MolesPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val sb = arrayListOf<String>()
        val profile = this.plugin.profileManager.getProfile(p)
        sb.add("")
        sb.add("&7Nick&8: &f${p.name}")
        when (profile.profileState) {
            FIGHTING -> {
                sb.add("")
                val match = this.plugin.matchManager.getMatch(profile)
                        ?: return sb
                sb.add("&aTwoj Team&8: &f${match.getMatchTeamByPlayer(p).getAliveCount()}")
                sb.add("&cPrzeciwnicy&8: &f${match.getOpponentTeam(p).getAliveCount()}")
                if (match.isState(MatchState.FIGHTING)) {
                    sb.add("")
                    sb.add("&7Koniec za&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                }
                sb.add("")
                sb.add(" &8* &fX: &e${match.arena.getDefendSpawn().x}")
                sb.add(" &8* &fZ: &e${match.arena.getDefendSpawn().z}")
            }
            LOBBY -> {
                val user = ToolsAPI.findUserByUUID(p.uniqueId)
                sb.add("&7Stan konta&8: &f${user.coins}")
                sb.add("")
                sb.add("&7Walczy&8: &f${this.plugin.queueManager.queueThread.fightPlayers}")
                sb.add("&7Kolejka&8: &f${this.plugin.queueManager.queueThread.queuePlayers}")
            }
            SPECTATING -> {
                sb.add("")
                val match = this.plugin.spectateManager.getMatch(p.uniqueId)
                        ?: return sb
                sb.add("&4Atakujący&8: &f${match.attackTeam.getAliveCount()}")
                sb.add("&9Broniący&8: &f${match.defendTeam.getAliveCount()}")
                if (match.isState(MatchState.FIGHTING)) {
                    sb.add("")
                    sb.add("&7Koniec&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                }
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