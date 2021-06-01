package pl.pvpcloud.event.scoreboard

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.EventState
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider

class EventScoreboardProvider(private val plugin: EventPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val s = arrayListOf<String>()
        s.add("")
        s.add("&7Nick&8: &f${p.name}")
        s.add("&7Ping&8: &f${(p as CraftPlayer).handle.ping}")
        val party = PartyAPI.getParty(p.uniqueId)
        if (party != null) {
            s.add("")
            s.add("&7Id&8: &f${party.id}")
            val event = this.plugin.eventManager.activeEvent
            if (event != null) {
                if (event.eventState == EventState.WAITING) {
                    s.add(if (event.partyWait.contains(party.id)) "&eZapisany" else "&cNiezapisany")
                } else {
                    if (event.parties.contains(party.id)) {
                        s.add("&cCzekają na walke&8: &f${event.parties.size}")
                    } else if (event.winners.contains(party.id)) {
                        s.add("&eWygrałeś")
                    }
                }
            }
        }
        s.add("")
        s.add(" &e&l* &fEvent")
        return s
    }
}