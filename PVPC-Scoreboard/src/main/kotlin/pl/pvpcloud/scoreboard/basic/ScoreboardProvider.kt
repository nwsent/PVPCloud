package pl.pvpcloud.scoreboard.basic

import org.bukkit.entity.Player

abstract class ScoreboardProvider {

    abstract fun getTitle(p: Player): String

    abstract fun getLines(p: Player): List<String>
}