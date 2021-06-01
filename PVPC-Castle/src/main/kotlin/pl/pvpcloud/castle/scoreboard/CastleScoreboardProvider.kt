package pl.pvpcloud.castle.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.match.MatchState
import pl.pvpcloud.castle.profile.ProfileState.*
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.tools.ToolsAPI

class CastleScoreboardProvider(private val plugin: CastlePlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val sb = arrayListOf<String>()
        val user = ToolsAPI.findUserByUUID(p.uniqueId)
        val profile = this.plugin.profileManager.getProfile(p)
        sb.add("")
        sb.add("&7Nick&8: &f${p.name}")
        sb.add("&7Stan konta&8: &f${user.coins}")
        sb.add("")
        when (profile.profileState) {
            FIGHTING -> {
                val match = this.plugin.matchManager.getMatch(profile)
                        ?: return sb
                sb.add("&aTwoj Team&8: &f${match.getMatchTeamByPlayer(p).getAliveCount()}")
                sb.add("&cPrzeciwnicy&8: &f${match.getOpponentTeam(p).getAliveCount()}")
                if (match.isState(MatchState.FIGHTING)) {
                    sb.add("")
                    sb.add("&7Koniec za&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                }
                sb.add("")
                sb.add("&4&lHp Zamku&8: &f${match.hp}")
            }
            LOBBY -> {
                sb.add("&7Walczy&8: &f${this.plugin.queueManager.queueThread.fightPlayers}")
                sb.add("&7Kolejka&8: &f${this.plugin.queueManager.queueThread.queuePlayers}")
            }
            SPECTATING -> {
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
                sb.add("&7Poczekaj aż gracze")
                sb.add("&7dołaczą do poczekalni.")
                sb.add("")
                val queueWait = this.plugin.queueManager.getQueueWait(p.uniqueId)
                if (queueWait != null) {
                    val playersSize = queueWait.getPlayersSize()
                    sb.add("&7Graczy&8: &f$playersSize/32")
                    if (playersSize >= this.plugin.config.minToStart) {
                        sb.add("&7Start za&8: &a${queueWait.countdown}")
                    } else {
                        sb.add("&7Potrzeba&8: &f${this.plugin.config.minToStart - playersSize}")
                    }
                }
            }
        }
        sb.add("")
        sb.add(" &e&l* &fCastle")
        return sb
    }
}