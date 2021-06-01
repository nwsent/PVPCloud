package pl.pvpcloud.statistics.server.variable

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import pl.pvpcloud.tablist.TablistAdapter

class TopDeathsVariable(name: String, private val place: Int) : TablistAdapter(name) {

    override fun replace(p0: Player): String {
        val player = (StatsAPI.plugin.statsRepository as StatsRepositoryServer).statsByDeaths
        if (player.size >= place) {
            return player[place - 1].name + " &8(&e${player[place - 1].deaths}&8)"
        }
        return place.toString()
    }
}