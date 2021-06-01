package pl.pvpcloud.statistics.server.variable

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tablist.TablistAdapter

class KDVariable(name: String) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        return StatsAPI.findPlayerStats(player).getKD().toString()
    }
}