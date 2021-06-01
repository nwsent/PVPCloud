package pl.pvpcloud.statistics.server.variable

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import pl.pvpcloud.tablist.TablistAdapter

class TopAssistsVariable(name: String, private val place: Int) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        val stats = (StatsAPI.plugin.statsRepository as StatsRepositoryServer).statsByAssists
        if (stats.size >= place) {
            return stats[place - 1].name + " &8(&e${stats[place - 1].assists}&8)"
        }
        return place.toString()
    }
}