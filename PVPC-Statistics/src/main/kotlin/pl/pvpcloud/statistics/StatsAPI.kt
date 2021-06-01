package pl.pvpcloud.statistics

import org.bukkit.entity.Player
import pl.pvpcloud.statistics.basic.PlayerStats
import pl.pvpcloud.statistics.server.repository.StatsRepositoryServer
import java.util.*
import java.util.stream.Collectors

object StatsAPI {

    internal lateinit var plugin: StatisticsPlugin

    fun findPlayerStats(uuid: UUID): PlayerStats {
        return this.plugin.statsRepository.getPlayerStatsByUUID(uuid)
                ?: throw NullPointerException("stats for $uuid is null")
    }

    fun findPlayerStats(player: Player): PlayerStats {
        return this.plugin.statsRepository.getPlayerStatsByUUID(player.uniqueId)
                ?: throw NullPointerException("stats for ${player.uniqueId} is null")
    }

    fun getCustomTops(id: Int, customId: Int): Int {
        return (this.plugin.statsRepository as StatsRepositoryServer).playerStatsMap.values.sortedByDescending { it.getValue(customId) }[id].getValue(customId)
    }

    fun getAndSetValue(uuid: UUID, id: Int, value: Int) {
        this.findPlayerStats(uuid).getAndSetValue(id, value)
    }

    fun getValue(uuid: UUID, id: Int): Int {
        return this.findPlayerStats(uuid).getValue(id)
    }

    fun incrementValue(uuid: UUID, id: Int) {
        this.findPlayerStats(uuid).incrementValue(id)
    }

    fun decrementValue(uuid: UUID, id: Int) {
        this.findPlayerStats(uuid).decrementValue(id)
    }

}