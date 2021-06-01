package pl.pvpcloud.moles.hub.variable

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tablist.TablistAdapter

class CustomStatsPlayerVariable(name: String, private val int: Int) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        return StatsAPI.getValue(player.uniqueId, int).toString()
    }

}