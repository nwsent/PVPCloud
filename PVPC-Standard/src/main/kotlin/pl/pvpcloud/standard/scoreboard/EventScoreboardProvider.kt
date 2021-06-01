package pl.pvpcloud.standard.scoreboard

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.standard.StandardPlugin

class EventScoreboardProvider(private val plugin: StandardPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        return arrayListOf(
                "",
                "&7Nick&8: &f${p.name}",
                "&7Ping&8: &f${(p as CraftPlayer).handle.ping}",
                "",
                " &e&l* &fStandard"
        )
    }
}