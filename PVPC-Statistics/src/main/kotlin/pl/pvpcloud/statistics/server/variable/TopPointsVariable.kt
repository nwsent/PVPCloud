package pl.pvpcloud.statistics.server.variable

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import pl.pvpcloud.tablist.TablistAdapter

class TopPointsVariable(name: String, private val place: Int) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        val stats = (StatsAPI.plugin.statsRepository as StatsRepositoryServer).statsByPoints
        if (stats.size >= place) {
            val userStats = stats[place - 1]
            return userStats.name + " &8(&e${userStats.fakePoints} &7[&f${userStats.lvls}&7] &8)"
        }
        return place.toString()
    }
}