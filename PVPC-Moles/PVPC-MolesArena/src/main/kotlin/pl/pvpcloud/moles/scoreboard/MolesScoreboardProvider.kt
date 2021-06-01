package pl.pvpcloud.moles.scoreboard

import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider

class MolesScoreboardProvider(private val plugin: MolesPlugin) : ScoreboardProvider() {

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
                    sb.add("&aTwoj Team&8: &f${match.getMatchTeamByPlayer(p).getAlivePlayersCount()}")
                    sb.add("&cPrzeciwnicy&8: &f${match.getOpponentTeam(p).getAlivePlayersCount()}")
                    if (match.isState(MatchState.FIGHTING)) {
                        sb.add("")
                        sb.add("&7Koniec za&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                    }
                    sb.add("")
                    sb.add(" &8* &fX: &e${match.arena.getDefendSpawn().x}")
                    sb.add(" &8* &fZ: &e${match.arena.getDefendSpawn().z}")
                }
            }
            GameMode.SPECTATOR -> {
                sb.add("")
                val match = this.plugin.matchManager.getMatchByWorld(p.world.uid)
                        ?: return sb
                sb.add("&4Atakujący&8: &f${match.attackTeam.getAlivePlayersCount()}")
                sb.add("&9Broniący&8: &f${match.defendTeam.getAlivePlayersCount()}")
                if (match.isState(MatchState.FIGHTING)) {
                    sb.add("")
                    sb.add("&7Koniec&8: &f${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}")
                }
            }
        }
        sb.add("")
        sb.add(" &e&l* &fKrety")
        return sb
    }
}