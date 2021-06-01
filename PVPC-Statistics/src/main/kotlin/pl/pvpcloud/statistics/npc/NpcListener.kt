package pl.pvpcloud.statistics.npc

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.statistics.StatisticsPlugin

class NpcListener(private val plugin: StatisticsPlugin) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        //if(player.name == "_sajmon360p") return player.kickPlayer("RELOG")

       // this.plugin.npcManager.spawnNPC(player)
    }


}