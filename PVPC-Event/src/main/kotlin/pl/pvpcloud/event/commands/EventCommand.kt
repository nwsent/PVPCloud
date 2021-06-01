package pl.pvpcloud.event.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.EventState
import pl.pvpcloud.party.PartyAPI
import java.util.stream.Collectors

@CommandAlias("event")
class EventCommand(private val plugin: EventPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) =
            sender.sendFixedMessage(arrayListOf(
                    "&8* &c/event zapisz",
                    "&8* &c/event list"
            ))

    @Subcommand("zapisz")
    fun onCommandAdd(sender: Player) {
        val event = this.plugin.eventManager.activeEvent
                ?: return sender.sendFixedMessage("&cBrak eventu")
        if (event.eventState != EventState.WAITING) {
            return sender.sendFixedMessage("&cEvent już wystartował")
        } else {
            val party = PartyAPI.getParty(sender.uniqueId)
                    ?: return sender.sendFixedMessage("&cNie posiadasz party, zobacz /party")
            if (party.uniqueIdLeader != sender.uniqueId) {
                return sender.sendFixedMessage("&cNie jesteś liderem")
            }
            if (event.partyWait.contains(party.id)) {
                sender.sendFixedMessage("&cJesteś już zapisany")
            } else {
                val partyMembers = party.members.stream().map { it.name }.collect(Collectors.joining("&8, &e"))

                event.partyWait.add(party.id)
                party.sendMessage(" &7» &eTwoje party zostało zapisane &a:)")
                party.sendMessage("    &8* &eOsoby które zostały zapisane: $partyMembers")
            }
        }
    }

    @Subcommand("list")
    fun onCommandList(sender: Player) {
        val event = this.plugin.eventManager.activeEvent
                ?: return sender.sendFixedMessage("&eBrak eventu")
        val sb = StringBuilder("&8* &7Gracze czekający na walke &8(&e${event.parties.size}&8) ")
        event.parties.mapNotNull { PartyAPI.getParty(it) }.forEach { sb.append("&e${it.id}&7, ") }
        sender.sendFixedMessage(sb.toString())
        val sbWin = StringBuilder("&8* &7Gracze wygrali runde &8(&e${event.winners.size}&8) ")
        event.winners.mapNotNull { PartyAPI.getParty(it) }.forEach { sbWin.append("&e${it.id}&7, ") }
        sender.sendFixedMessage(sbWin.toString())
    }

}