package pl.pvpcloud.xvsx.arena.match

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.xvsx.arena.match.MatchState.*
import pl.pvpcloud.xvsx.arena.match.event.MatchStartEvent

class MatchTask(private val match: Match) : BukkitRunnable() {

    init {
        runTaskTimer(match.plugin, 0, 20)
    }

    override fun run() {
        when(match.matchState) {
            WAITING -> {
                if (match.decrementCountdown() == 0) {
                    match.countdown = 4
                    match.matchState = STARTING
                } else {
                    when (match.countdown) {
                        1 -> {
                            Bukkit.getPluginManager().callEvent(MatchStartEvent(this.match))
                        }
                        3 -> {
                            this.match.plugin.profileManager.getProfilesBy { it.matchId == match.id }.forEach {
                                ConnectAPI.getPlayerByUUID(it.uniqueId)?.connect(NetworkAPI.id)
                            }
                        }
                    }
                }
            }
            STARTING -> {
                if (match.decrementCountdown() == 0) {
                    match.matchState = FIGHTING
                } else {
                    match.sendTitle("&6${match.countdown}")
                }
            }
            FIGHTING -> {
                //todo calc player
            }
            SWITCHING -> {
                if (match.decrementCountdown() == 0) {
                    //todo jakby nastepna runda
                }
            }
            ENDING -> {
                this.cancel()
                return
            }
        }
    }

}