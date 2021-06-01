package pl.pvpcloud.statistics.server.repository

import org.bukkit.entity.Player
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.statistics.api.StatsRepository
import pl.pvpcloud.statistics.basic.CustomStats
import pl.pvpcloud.statistics.basic.PlayerStats
import pl.pvpcloud.statistics.server.basic.StatsComparator
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class StatsRepositoryServer(private val plugin: StatisticsPlugin) : StatsRepository {

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

    private val comparator = StatsComparator()

    private val customStats = ConcurrentHashMap<Int, CustomStats>()

    internal val statsByKills get() = this.playerStatsMap.values.sortedByDescending { it.kills }
    internal val statsByDeaths get() = this.playerStatsMap.values.sortedByDescending { it.deaths }
    internal val statsByAssists get() = this.playerStatsMap.values.sortedByDescending { it.assists }
    internal val statsByPoints get() = this.playerStatsMap.values.sortedByDescending {
        val userStats = it
        val userPkt = userStats.points + (userStats.lvls * 1000)
        it.fakePoints = userPkt

        it.fakePoints
    }
    internal val statsByLevels get() = this.playerStatsMap.values.sortedByDescending { it.lvls }

    init {
        DatabaseAPI.loadAll<PlayerStats>(this.plugin.config.collectionName) { listPlayers ->
            listPlayers.forEach {
                this.playerStatsMap[it.uuid] = it
            }
            sortRankings()
        }
    }

    fun createPlayerStats(uniqueId: UUID, name: String): PlayerStats {
        val playerStats = PlayerStats(
                uniqueId,
                name,
                1000, 0, 0, 0, ConcurrentHashMap()
        )
        customStats.forEach {
            val stats = CustomStats(it.value.name, it.value.value)
            playerStats.customStats[it.key] = stats
        }
        playerStats.insertEntity()
        this.playerStatsMap[playerStats.uuid] = playerStats
        return playerStats
    }

    fun resetPlayerStats(player: Player) {
        val playerStats = this.getPlayerStatsByUUID(player.uniqueId)
                ?: return
        playerStats.fakePoints = 1000
        playerStats.points = 1000
        playerStats.kills = 0
        playerStats.deaths = 0
        playerStats.assists = 0
        playerStats.lvls = 0
        playerStats.customStats.forEach { (t: Int, u: CustomStats) ->
            u.value = 0
        }
        playerStats.updateEntity()
    }


    fun sortRankings() = this.playerStatsMap.values.sortedWith(this.comparator)
}