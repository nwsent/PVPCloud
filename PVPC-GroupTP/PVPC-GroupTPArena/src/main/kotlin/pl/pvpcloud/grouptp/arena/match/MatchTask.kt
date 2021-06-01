package pl.pvpcloud.grouptp.arena.match

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.grouptp.arena.match.MatchState.*
import pl.pvpcloud.grouptp.arena.match.event.MatchEndEvent
import pl.pvpcloud.grouptp.arena.match.event.MatchStartEvent
import java.util.concurrent.TimeUnit

class MatchTask(private val match: Match) : BukkitRunnable() {

    init {
        this.runTaskTimer(this.match.plugin, 0, 20)
    }

    override fun run() {
        when (this.match.matchState) {
            STARTING -> {
                if (this.match.decrementCountdown() == 0) {
                    this.match.timeToEnd = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(8)
                    this.match.matchState = FIGHTING
                } else {
                    if (this.match.countdown == 1) {
                        Bukkit.getPluginManager().callEvent(MatchStartEvent(match))
                    }
                    if (this.match.countdown == 3) {
                        match.players.forEach {
                            ConnectAPI.getPlayerByUUID(it)?.connect("gtp_1")
                        }
                    }
                }
            }
            FIGHTING -> {
                val alive = this.match.getAlivePlayersCount()
                if (this.match.timeToEnd < System.currentTimeMillis()) {
                    this.match.sendMessage("&4&lUpsik! &cKoniec czasu mecz wygrywa losowa osoba")
                    if (alive >= 1) {
                        this.match.plugin.server.pluginManager.callEvent(
                                MatchEndEvent(
                                        this.match.getPlayersMatch().first().uniqueId,
                                        this.match
                                )
                        )
                    } else if (alive < 1) {
                        this.match.plugin.server.pluginManager.callEvent(
                                MatchEndEvent(
                                        null,
                                        this.match
                                )
                        )
                    }
                    return
                }
                if (alive <= 1) {
                    if (alive == 1) {
                        this.match.plugin.server.pluginManager.callEvent(
                                MatchEndEvent(
                                        this.match.getPlayersMatch().first().uniqueId,
                                        this.match
                                )
                        )
                    } else if (alive < 1) {
                        this.match.plugin.server.pluginManager.callEvent(
                                MatchEndEvent(
                                        null,
                                        this.match
                                )
                        )
                    }
                    return
                }
            }
            ENDING -> {
                this.cancel()
                return
            }
        }
    }
}