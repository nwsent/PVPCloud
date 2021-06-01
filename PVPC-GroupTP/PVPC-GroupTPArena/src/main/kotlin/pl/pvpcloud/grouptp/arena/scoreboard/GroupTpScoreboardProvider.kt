package pl.pvpcloud.grouptp.arena.scoreboard

import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.arena.match.MatchState
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider

class GroupTpScoreboardProvider(private val plugin: GroupTpPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val sb = arrayListOf<String>()
        sb.add("")
        sb.add("&7Nick&8: &f${p.name}")
        when (p.gameMode) {
            GameMode.SURVIVAL -> {
                val match = this.plugin.matchManager.getMatch(p)
                if (match != null) {
                    sb.add("")
                    sb.add("&cPrzeciwnicy&8: &f${match.getAlivePlayersCount()-1}")
                    if (match.isState(MatchState.FIGHTING)) {
                        sb.add("")
                        sb.add("&7Koniec za&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                    }
                    sb.add("")
                }
            }
        }
        sb.add("")
        sb.add(" &e&l* &fGrupoweTP")
        return sb
    }
}