package pl.pvpcloud.xvsx.arena.scoreboard

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.xvsx.arena.XvsXPlugin

class XvsXScoreboardProvider(private val plugin: XvsXPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        return arrayListOf(
                "",
                "&7Nick&8: &f${p.name}",
                "&7Ping&8: &f${(p as CraftPlayer).handle.ping}",
                "",
                "&7Czas&8: &f0:0s}",
                "",
                " &e&l* &fXvsX"
        )
    }
}