package pl.pvpcloud.statistics.config

import pl.pvpcloud.statistics.api.PluginType
import pl.pvpcloud.statistics.basic.CustomStats

class StatisticsConfig {

    val pluginTyp: PluginType = PluginType.SERVER

    val serversToUpdate = arrayOf("moles_hub")

    val collectionName: String = "stats-Castle"

    val customStats = hashMapOf(
        Pair(0, CustomStats("allMatch", 0)),
        Pair(1, CustomStats("winMatch", 0)),
        Pair(2, CustomStats("loseMatch",0))
    )

    val replaceCustomStatsName = hashMapOf(
        Pair("%allMatch%", 0),
        Pair("%winMatch%", 1),
        Pair("%loseMatch%", 2)
    )

    val playerStatsInfo = arrayListOf(
            "&7Gracz &6%name%",
            "&7Punkty: &6%points%",
            "&7Zabojstwa: &6%kills%",
            "&7Zgony: &6%deaths%",
            "&7Asysty: &6%assists%",
            "",
            "&7Rozegrane mecze: %allMatch%",
            "&7Wygrane mecze: %winMatch%",
            "&7Przegrane mecze: %loseMatch%"
    )

}