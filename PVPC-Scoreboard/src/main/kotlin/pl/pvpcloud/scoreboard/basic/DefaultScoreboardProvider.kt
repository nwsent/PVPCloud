package pl.pvpcloud.scoreboard.basic

import org.bukkit.entity.Player

class DefaultScoreboardProvider : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&fPVP&eCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        return arrayListOf("")
    }
}