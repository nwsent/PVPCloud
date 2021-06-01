package pl.pvpcloud.event.events.cage.match

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.events.EventHelper
import pl.pvpcloud.event.events.cage.CageEvent
import pl.pvpcloud.party.basic.Party

class CageMatch(
        private val cageEvent: CageEvent,
        val partyA: Party,
        val partyB: Party
) : BukkitRunnable() {

    var countdown = 6

    init {
        runTaskTimer(this.cageEvent.plugin, 0, 20)
    }

    fun getPlayers(): List<Player> {
        return (getPlayersTeamA() + getPlayersTeamB())
    }

    fun getPlayersTeamA(): List<Player> {
        return partyA.membersOnline().toList().take(cageEvent.eventType.size)
    }

    fun getPlayersTeamB(): List<Player> {
        return partyB.membersOnline().toList().take(cageEvent.eventType.size)
    }

    override fun run() {
        this.countdown--
        if (this.countdown == 5) {

            val spawnA = this.cageEvent.plugin.config.spawnLocationSpawnACage.toBukkitLocation()
            val spawnB = this.cageEvent.plugin.config.spawnLocationSpawnBCage.toBukkitLocation()

            this.getPlayersTeamA().forEach {
                it.teleport(spawnA)
            }

            this.getPlayersTeamB().forEach {
                it.teleport(spawnB)
            }

            this.getPlayersTeamA().forEach {
                EventHelper.pickPlayer(it, this.cageEvent)
            }
            this.getPlayersTeamB().forEach {
                EventHelper.pickPlayer(it, this.cageEvent)
            }
        }
        if (this.countdown == 4) {
            val spawnA = this.cageEvent.plugin.config.spawnLocationSpawnACage.toBukkitLocation()
            val spawnB = this.cageEvent.plugin.config.spawnLocationSpawnBCage.toBukkitLocation()

            this.getPlayersTeamA().forEach {
                it.teleport(spawnA)
            }

            this.getPlayersTeamB().forEach {
                it.teleport(spawnB)
            }
        }
        if (this.countdown == 0) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendTitle("", "&eSTART", 5, 35, 5)
            }
            this.cancel()
        } else if (this.countdown <= 3) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendTitle("", "&6${this.countdown}", 5, 20, 5)
            }
        }
    }

}