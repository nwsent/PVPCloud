package pl.pvpcloud.event.events.cage

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.cage.match.CageMatch
import pl.pvpcloud.event.events.Event
import pl.pvpcloud.event.events.EventHelper
import pl.pvpcloud.event.events.EventState
import pl.pvpcloud.event.events.EventType
import pl.pvpcloud.fight.FightAPI
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.party.basic.Party

class CageEvent(plugin: EventPlugin, eventType: EventType) : Event(plugin, "Klatki", eventType) {

    var cageMatch: CageMatch? = null

    var cageState = CageState.STARTING

    enum class CageState {
        STARTING,
        FIGHTING,
        SWITCHING,
        ENDING
    }

    override fun start() {
        this.eventState = EventState.STARTED
        CageTask(this)
    }

    class CageTask(val cage: CageEvent) : BukkitRunnable() {

        init {
            runTaskTimer(this.cage.plugin, 0, 20)
        }

        override fun run() {
            when (cage.cageState) {
                CageState.STARTING -> {
                    if (cage.partyWait.size < 2) {
                        this.cancel()
                        return
                    }
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendTitle("", "&aStartujemy", 5, 30, 5)
                    }
                    cage.partyWait.mapNotNull { PartyAPI.getParty(it) }.forEach {
                        cage.parties.add(it.id)
                    }
                    cage.cageState = CageState.SWITCHING
                }
                CageState.SWITCHING -> {
                    if (--cage.countdown != 0)
                        return

                    val validParties: MutableList<Party> = this.cage.parties.mapNotNull { PartyAPI.getParty(it) }.filter { it.membersOnline().count() > 0 }.toMutableList()
                    cage.parties.clear()
                    cage.parties.addAll(validParties.map { it.id })

                    if (cage.winners.size == 1 && cage.parties.size == 0) {
                        cage.cageState = CageState.ENDING
                        return
                    } else if (cage.parties.size == 1 && cage.winners.size >= 1) {
                        cage.winners.addAll(cage.parties)
                        val luckyParty = validParties.first()
                        cage.parties.clear()
                        Bukkit.broadcastMessage("&aParty gracza &6${luckyParty.id} &adostało sie dalej ponieważ nie miało przeciwnika".fixColors())
                        Bukkit.broadcastMessage("")
                        Bukkit.broadcastMessage("&eKlatki &6Nastepna tura startuje...".fixColors())
                        Bukkit.broadcastMessage("")
                        cage.parties.addAll(cage.winners)
                        cage.winners.clear()
                    } else if (cage.parties.size == 0) {
                        cage.parties.clear()
                        Bukkit.broadcastMessage("")
                        Bukkit.broadcastMessage("&eKlatki &6Nastepna tura startuje...".fixColors())
                        Bukkit.broadcastMessage("")
                        cage.parties.addAll(cage.winners)
                        cage.winners.clear()
                    }

                    val validPartiesAfter: MutableList<Party> = this.cage.parties.mapNotNull { PartyAPI.getParty(it) }.filter { it.membersOnline().count() > 0 }.toMutableList()

                    val teamA = validPartiesAfter.first()
                    val teamB = validPartiesAfter.minus(teamA).first()
                    val cageMatch = CageMatch(this.cage, teamA, teamB)
                    this.cage.cageMatch = cageMatch
                    this.cage.parties.remove(teamA.id)
                    this.cage.parties.remove(teamB.id)
                    cage.countdown = 2
                    cage.cageState = CageState.FIGHTING
                }
                CageState.FIGHTING -> {
                    val cageMatch = cage.cageMatch
                    if (cageMatch == null) {
                        cage.countdown = 2
                        cage.cageState = CageState.SWITCHING
                        return
                    }

                    if (cageMatch.getPlayersTeamA().none { it.gameMode == GameMode.ADVENTURE }) {
                        cage.winners.add(cageMatch.partyB.id)
                        Bukkit.broadcastMessage("&8* &eParty &6${cageMatch.partyB.id} &eprzechodzi dalej".fixColors())
                        cageMatch.getPlayers().forEach {
                            EventHelper.specPlayer(it)
                            FightAPI.getFight(it.uniqueId).clear()
                        }
                        cage.cageMatch = null
                    } else if (cageMatch.getPlayersTeamB().none { it.gameMode == GameMode.ADVENTURE }) {
                        cage.winners.add(cageMatch.partyA.id)
                        Bukkit.broadcastMessage("&8* &eParty &6${cageMatch.partyA.id} &eprzechodzi dalej".fixColors())
                        cageMatch.getPlayers().forEach {
                            EventHelper.specPlayer(it)
                            FightAPI.getFight(it.uniqueId).clear()
                        }
                        cage.cageMatch = null
                    }
                }
                CageState.ENDING -> {
                    val loc = cage.plugin.config.spawnLocation.toBukkitLocation()
                    val winNames = StringBuilder("")
                    cage.winners.mapNotNull { PartyAPI.getParty(it) }.forEach { party ->
                        party.membersOnline().forEach {
                            EventHelper.specPlayer(it)
                            winNames.append("${it.name}, ")
                            it.teleport(loc)
                        }
                    }
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendFixedMessage("&aGratulacje! &fKlatki wygrywają: &e$winNames")
                        it.sendTitle("&aGratulacje!", "&fKlatki wygrywają: &e$winNames", 20, 120, 20)
                    }
                    this.cage.plugin.eventManager.activeEvent = null
                    this.cancel()
                }
            }
        }
    }

}