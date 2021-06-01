package pl.pvpcloud.castle.match

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.castle.match.MatchState.*
import pl.pvpcloud.castle.match.event.MatchEndEvent
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendActionBar
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.save.KitType
import pl.pvpcloud.save.SaveAPI
import pl.pvpcloud.tag.TagAPI
import java.util.concurrent.TimeUnit

class MatchTask(private val match: Match) : BukkitRunnable() {

    init {
        this.runTaskTimer(this.match.plugin, 0, 20)
    }

    override fun run() {
        when (this.match.matchState) {
            VOTING -> {
                if (this.match.decrementCountdown() == 0) {
                    this.match.getPlayersMatch().forEach {
                        it.closeInventory()
                    }
                    this.match.handleEndVote()
                    this.match.countdown = 6
                    this.match.matchState = STARTING
                    this.match.getPlayersAttackAlive().forEach {
                        SaveAPI.giveItems(it, this.match.winKitName, KitType.ATTACK)
                    }
                    this.match.getPlayersDefendAlive().forEach {
                        SaveAPI.giveItems(it, this.match.winKitName, KitType.DEFEND)
                    }
                    this.match.getPlayersMatch().forEach {
                        TagAPI.refresh(it)
                    }
                } else {
                    this.match.sendTitle("", this.match.plugin.config.messages.matchCountdownVote.rep("%time%", this.match.countdown.toString()))
                }
            }
            STARTING -> {
                if (this.match.decrementCountdown() == 0) {
                    this.match.timeToEnd = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20)
                    this.match.lastPlaceTnt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3)
                    this.match.matchState = FIGHTING
                    this.match.sendTitle("", "&aGo! Go! Go!")
                } else {
                    this.match.sendTitle("", this.match.plugin.config.messages.matchCountdownStart.rep("%time%", this.match.countdown.toString()))
                }
            }
            FIGHTING -> {
                val attackPlayers = this.match.getPlayersAttackAlive()
                        .filter { !FightAPI.isFighting(it.uniqueId) }
                        .filter { this.match.arena.isInCenter(it.location, 3, 1, 3) }
                if (attackPlayers.count() > 0) {
                    this.match.attacking = true
                    this.match.hp -= attackPlayers.count()
                } else {
                    this.match.attacking = false
                }
                if (this.match.attacking) {
                    if (this.match.timeToEnd < System.currentTimeMillis()) {
                        this.match.timeToEnd += TimeUnit.MINUTES.toMillis(1)
                        this.match.sendMessage("&4&lUpsik! &cKoniec czasu ale jest przejmowanie wiec dodajemy &4minute")
                    }
                    this.match.getPlayersWorld().forEach {
                        it.sendActionBar("&4&lHP ZAMKU&8: &f${this.match.hp}")
                    }
                    if (this.match.hp <= 0) {
                        this.match.plugin.server.pluginManager.callEvent(
                            MatchEndEvent(
                                this.match.attackTeam,
                                this.match.defendTeam,
                                match
                            )
                        )
                    }
                } else {
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
                }
            }
            ENDING -> {
                this.cancel()
                return
            }
        }
    }
}