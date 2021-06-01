package pl.pvpcloud.moles.match

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.moles.match.MatchState.*
import pl.pvpcloud.moles.match.event.MatchEndEvent
import pl.pvpcloud.save.KitAPI
import pl.pvpcloud.shop.ShopAPI
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
                    this.match.getPlayersMatchAlive().mapNotNull { Bukkit.getPlayer(it.uuid) }.forEach {
                        it.closeInventory()
                    }
                    this.match.handleEndVote()
                    this.match.countdown = 6
                    this.match.matchState = STARTING
                    val winKit = KitAPI.getKit(this.match.winKitName)
                    this.match.getPlayersAllAlive().forEach {
                        KitAPI.giveKit(it, winKit)
                        ShopAPI.giveItems(it)
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
            }
            ENDING -> {
                this.cancel()
                return
            }
        }
    }
}