package pl.pvpcloud.event.events.sumo

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.Event
import pl.pvpcloud.event.events.EventHelper
import pl.pvpcloud.event.events.EventType
import pl.pvpcloud.event.events.sumo.match.SumoMatch
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.party.basic.Party

class SumoEvent(plugin: EventPlugin, eventType: EventType) : Event(plugin, "Sumo", eventType) {

    var sumoMatch: SumoMatch? = null

    var sumoState = SumoState.STARTING

    enum class SumoState {
        STARTING,
        FIGHTING,
        SWITCHING,
        ENDING
    }

    override fun start() {
        SumoTask(this)
    }

    class SumoTask(val sumo: SumoEvent) : BukkitRunnable() {

        init {
            runTaskTimer(this.sumo.plugin, 0, 20)
        }

        override fun run() {
            when (sumo.sumoState) {
                SumoState.STARTING -> {
                    if (sumo.partyWait.size < 2) {
                        this.cancel()
                        return
                    }
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendTitle("", "&aStartujemy", 5, 30, 5)
                    }
                    sumo.partyWait.mapNotNull { PartyAPI.getParty(it) }.forEach {
                        sumo.parties.add(it.id)
                    }
                    sumo.sumoState = SumoState.SWITCHING
                }
                SumoState.SWITCHING -> {
                    if (--sumo.countdown != 0)
                        return

                    val validParties: MutableList<Party> = this.sumo.parties.mapNotNull { PartyAPI.getParty(it) }.filter { it.membersOnline().count() > 0 }.toMutableList()
                    sumo.parties.clear()
                    sumo.parties.addAll(validParties.map { it.id })

                    if (sumo.winners.size == 1 && sumo.parties.size == 0) {
                        sumo.sumoState = SumoState.ENDING
                        return
                    } else if (sumo.parties.size == 1 && sumo.winners.size >= 1) {
                        sumo.winners.addAll(sumo.parties)
                        val luckyParty = validParties.first()
                        sumo.parties.clear()
                        Bukkit.broadcastMessage("&aParty gracza &6${luckyParty.leaderMember.name} &adostało sie dalej ponieważ nie miało przeciwnika".fixColors())
                        sumo.parties.addAll(sumo.winners)
                        sumo.winners.clear()
                    } else if (sumo.parties.size == 0) {
                        sumo.parties.clear()
                        Bukkit.broadcastMessage("")
                        Bukkit.broadcastMessage("&eSumo &6Nastepna tura startuje...".fixColors())
                        Bukkit.broadcastMessage("")
                        sumo.parties.addAll(sumo.winners)
                        sumo.winners.clear()
                    }

                    val validPartiesAfter: MutableList<Party> = this.sumo.parties.mapNotNull { PartyAPI.getParty(it) }.filter { it.membersOnline().count() > 0 }.toMutableList()

                    val teamA = validPartiesAfter.random()
                    val teamB = validPartiesAfter.minus(teamA).random()
                    val sumoMatch = SumoMatch(this.sumo, teamA, teamB)
                    this.sumo.sumoMatch = sumoMatch
                    this.sumo.parties.remove(teamA.id)
                    this.sumo.parties.remove(teamB.id)
                    sumo.countdown = 2
                    sumo.sumoState = SumoState.FIGHTING
                }
                SumoState.FIGHTING -> {
                    val sumoMatch = sumo.sumoMatch
                    if (sumoMatch == null) {
                        sumo.countdown = 2
                        sumo.sumoState = SumoState.SWITCHING
                        return
                    }

                    if (sumoMatch.getPlayersTeamA().none { it.location.y > 50 }) {
                        sumo.winners.add(sumoMatch.partyB.id)
                        Bukkit.broadcastMessage("&8* &eParty &6${sumoMatch.partyB.leaderMember.name} &eprzechodzi dalej".fixColors())
                        sumoMatch.getPlayers().forEach {
                            EventHelper.specPlayer(it)
                            FightAPI.getFight(it.uniqueId).clear()
                        }
                        sumo.sumoMatch = null
                    } else if (sumoMatch.getPlayersTeamB().none { it.location.y > 50 }) {
                        sumo.winners.add(sumoMatch.partyA.id)
                        Bukkit.broadcastMessage("&8* &eParty &6${sumoMatch.partyA.leaderMember.name} &eprzechodzi dalej".fixColors())
                        sumoMatch.getPlayers().forEach {
                            EventHelper.specPlayer(it)
                            FightAPI.getFight(it.uniqueId).clear()
                        }
                        sumo.sumoMatch = null
                    }
                }
                SumoState.ENDING -> {
                    val loc = sumo.plugin.config.spawnLocation.toBukkitLocation()
                    val winNames = StringBuilder("")
                    sumo.winners.mapNotNull { PartyAPI.getParty(it) }.forEach { party ->
                        party.membersOnline().forEach {
                            EventHelper.specPlayer(it)
                            winNames.append("${it.name}, ")
                            it.teleport(loc)
                        }
                    }
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendFixedMessage("&aGratulacje! &fSumo wygrywają: &e$winNames")
                        it.sendTitle("&aGratulacje!", "&fSumo wygrywają: &e$winNames", 20, 120, 20)
                    }
                    this.cancel()
                }
            }
        }
    }

}