package pl.pvpcloud.statistics.linker.repository

import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.api.StatsRepository
import pl.pvpcloud.statistics.basic.CustomStats
import pl.pvpcloud.statistics.basic.PlayerStats
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class StatsRepositoryLinker(private val plugin: StatisticsPlugin) : StatsRepository {

    override val playerStatsMap: ConcurrentHashMap<UUID, PlayerStats> = ConcurrentHashMap()

    override fun getPlayerStatsByUUID(uniqueId: UUID): PlayerStats? {
        return this.playerStatsMap[uniqueId]
    }

    override fun getPlayerStatsByNick(nickname: String): PlayerStats? {
        return this.playerStatsMap.values.singleOrNull { it.name == nickname }
    }

    override fun registerCustomStats(id: Int, customStats: CustomStats) {
        this.customStats[id] = customStats
    }

    private val customStats = ConcurrentHashMap<Int, CustomStats>()
}