package pl.pvpcloud.moles.match

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.moles.match.MatchState.*
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.moles.match.event.MatchEndEvent
import pl.pvpcloud.moles.match.event.MatchStartEvent
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tag.TagAPI
import java.util.concurrent.TimeUnit

class MatchTask(private val match: Match) : BukkitRunnable() {

    init {
        this.runTaskTimer(this.match.plugin, 0, 20)
    }

    override fun run() {
        when (this.match.matchState) {
            WAITING -> {
                if (this.match.decrementCountdown() == 0) {
                    Bukkit.getPluginManager().callEvent(MatchStartEvent(match))
                    this.match.countdown = 6
                    this.match.matchState = VOTING
                } else if (this.match.countdown == 2) {
                    this.match.attackTeam.players.forEach {
                        ConnectAPI.getPlayerByUUID(it)?.connect(NetworkAPI.id)
                    }
                    this.match.defendTeam.players.forEach {
                        ConnectAPI.getPlayerByUUID(it)?.connect(NetworkAPI.id)
                    }
                }
            }
            VOTING -> {
                if (this.match.decrementCountdown() == 0) {
                    this.match.getPlayersMatch().forEach {
                        it.closeInventory()
                    }
                    this.match.handleEndVote()
                    this.match.countdown = 4
                    this.match.matchState = STARTING
                    val kitProfile = this.match.plugin.kitManager.getKitProfile(this.match.winKitProfileName)
                            ?: this.match.plugin.kitManager.kits.first()
                    this.match.getPlayersMatch().forEach {
                        this.match.plugin.kitManager.giveKit(it, kitProfile.kits.random())
                        match.plugin.shopModule.giveItems(it)
                        TagAPI.refresh(it)
                    }
                } else {
                    this.match.sendTitle("", this.match.plugin.config.messages.matchCountdownVote.rep("%time%", this.match.countdown.toString()))
                }
            }
            STARTING -> {
                if (this.match.decrementCountdown() == 0) {
                    this.match.timeToEnd = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(8)
                    this.match.matchState = FIGHTING
                    this.match.sendTitle("", "&aGo! Go! Go!")
                    this.match.attackTeam.sendMessage(" &8* &fKordynaty bazy&8: " +
                            "&e${this.match.arena.getDefendSpawn().x}&8, " +
                            "&e${this.match.arena.getDefendSpawn().y}&8, " +
                            "&e${this.match.arena.getDefendSpawn().z}")
                } else {
                    this.match.sendTitle("", this.match.plugin.config.messages.matchCountdownStart.rep("%time%", this.match.countdown.toString()))
                }
            }
            FIGHTING -> {
                if (this.match.timeToEnd < System.currentTimeMillis()) {
                    this.match.sendMessage("&4&lUpsik! &cKoniec czasu mecz wygrywa drużyna broniących")
                    this.match.plugin.server.pluginManager.callEvent(
                            MatchEndEvent(
                                    this.match.defendTeam,
                                    this.match.attackTeam,
                                    match
                            )
                    )
                }
                val aliveA = this.match.attackTeam.getAlivePlayersCount()
                val aliveD = this.match.defendTeam.getAlivePlayersCount()
                if (aliveA == 0 && aliveD != 0) {
                    this.match.plugin.server.pluginManager.callEvent(
                            MatchEndEvent(
                                    this.match.defendTeam,
                                    this.match.attackTeam,
                                    match
                            )
                    )
                    return
                } else if (aliveA != 0 && aliveD == 0) {
                    this.match.plugin.server.pluginManager.callEvent(
                            MatchEndEvent(
                                    this.match.attackTeam,
                                    this.match.defendTeam,
                                    match
                            )
                    )
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