package pl.pvpcloud.statistics.server.basic

import pl.pvpcloud.statistics.basic.PlayerStats

internal class StatsComparator : Comparator<PlayerStats> {

    override fun compare(o1: PlayerStats, o2: PlayerStats) = o2.points.compareTo(o1.points)

}