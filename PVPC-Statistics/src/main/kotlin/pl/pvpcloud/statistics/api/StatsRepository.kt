package pl.pvpcloud.statistics.api

import pl.pvpcloud.statistics.basic.CustomStats
import pl.pvpcloud.statistics.basic.PlayerStats
import java.util.*
import java.util.concurrent.ConcurrentMap

interface StatsRepository {

    val playerStatsMap: ConcurrentMap<UUID, PlayerStats>

    fun getPlayerStatsByUUID(uniqueId: UUID): PlayerStats?
    fun getPlayerStatsByNick(nickname: String): PlayerStats?

    fun registerCustomStats(id: Int, customStats: CustomStats)


}