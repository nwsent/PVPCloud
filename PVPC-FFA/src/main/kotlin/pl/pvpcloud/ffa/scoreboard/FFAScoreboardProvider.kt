package pl.pvpcloud.ffa.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI

class FFAScoreboardProvider(private val plugin: FFAPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val stats = StatsAPI.findPlayerStats(p)
        val user = ToolsAPI.findUserByUUID(p.uniqueId)
        val sb = arrayListOf<String>()
        sb.add("")
        sb.add(" &7Nick&8: &f${p.name}")
        sb.add(" &7Stan konta&8: &f${user.coins}")
        sb.add("")
        sb.add(" &7Punktów&8: &e${stats.points}")
        sb.add(" &7Zabójstw&8: &e${stats.kills}")
        sb.add(" &7Śmierci&8: &e${stats.deaths}")
        sb.add(" &7Asyst&8: &e${stats.assists}")
        sb.add("")
        sb.add(" &e&l* &fFFA")
        return sb
    }

}