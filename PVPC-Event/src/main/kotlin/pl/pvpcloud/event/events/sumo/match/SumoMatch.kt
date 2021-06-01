package pl.pvpcloud.event.events.sumo.match

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.events.EventHelper
import pl.pvpcloud.event.events.sumo.SumoEvent
import pl.pvpcloud.party.basic.Party

class SumoMatch(
        private val sumoEvent: SumoEvent,
        val partyA: Party,
        val partyB: Party
) : BukkitRunnable() {

    var countdown = 3

    init {
        runTaskTimer(this.sumoEvent.plugin, 0, 20)
    }

    fun getPlayers(): List<Player> {
        return (getPlayersTeamA() + getPlayersTeamB())
    }

    fun getPlayersTeamA(): List<Player> {
        return partyA.membersOnline().toList().take(sumoEvent.eventType.size)
    }

    fun getPlayersTeamB(): List<Player> {
        return partyB.membersOnline().toList().take(sumoEvent.eventType.size)
    }

    override fun run() {
        this.countdown--
        if (this.countdown == 2) {

            val spawnA = this.sumoEvent.plugin.config.spawnLocationSpawnASumo.toBukkitLocation()
            val spawnB = this.sumoEvent.plugin.config.spawnLocationSpawnBSumo.toBukkitLocation()

            Bukkit.getOnlinePlayers().forEach {
                it.sendTitle("", "&6${this.partyA.leaderMember.name} &7vs &6${this.partyB.leaderMember.name}", 5, 35, 5)
            }
            this.getPlayersTeamA().forEach {
                it.teleport(spawnA)
            }
            this.getPlayersTeamB().forEach {
                it.teleport(spawnB)
            }
            this.getPlayersTeamA().forEach {
                EventHelper.pickPlayer(it, this.sumoEvent)
            }
            this.getPlayersTeamB().forEach {
                EventHelper.pickPlayer(it, this.sumoEvent)
            }
        }
        if (this.countdown == 0) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendTitle("", "&eSTART", 5, 35, 5)
            }
            this.cancel()
        }
    }
}