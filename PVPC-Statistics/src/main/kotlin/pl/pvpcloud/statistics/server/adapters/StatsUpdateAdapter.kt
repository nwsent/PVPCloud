package pl.pvpcloud.statistics.server.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.statistics.StatisticsPlugin
import pl.pvpcloud.statistics.api.packets.PacketStatsUpdate
import pl.pvpcloud.statistics.basic.PlayerStats

class StatsUpdateAdapter(private val plugin: StatisticsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketStatsUpdate && packet.collectionName == this.plugin.config.collectionName) {
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                DatabaseAPI.loadBySelector<PlayerStats>(this.plugin.config.collectionName, packet.uniqueId.toString(), "uuid") {
                    if (it != null) {
                        this.plugin.statsRepository.playerStatsMap[it.uuid] = it
                    }
                }
            }, 50)
        }
    }
}