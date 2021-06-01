package pl.pvpcloud.party.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.gui.PartyGui

@CommandAlias("party")
class PartyCommand(private val partyModule: PartyModule) : BaseCommand() {

    @Subcommand("help|pomoc")
    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) =
        sender.sendFixedMessage(arrayListOf(
                " ",
                " &8)&m-------------&8&l|&7»  &eParty  &7«&8&l|&8&m-------------&r&8(",
                " ",
                " &e/party zaloz &8- &fTworzy party",
                " &e/party usun &8- &fUsuwa party",
                " &e/party opusc &8- &fOpuszcza party",
                " &e/party dolacz &8<&fnick&8> &8- &fDołącza do party",
                " &e/party zapros &8<&fnick&8> &r&8- &fZaprasza gracza do party",
                " &e/party wyrzuc &8<&fnick&8> &r&8- &fWyrzuca gracza z party",
                " ",
                " &e/party info &8- &fInformacjie o party",
                " ",
                " &8)&m-------------&8&l|&7»  &eParty  &7«&8&l|&8&m-------------&r&8("
        ))


    @Subcommand("zaloz")
    fun onCreate(sender: Player) =
            this.partyModule.partyFactory.createParty(sender)

    @Subcommand("usun")
    fun onDelete(sender: Player) =
            this.partyModule.partyFactory.deleteParty(sender)

    @Subcommand("opusc")
    fun onLeave(sender: Player) =
            this.partyModule.partyFactory.leaveParty(sender, false)

    @Subcommand("dolacz")
    @Syntax("<nick>")
    fun onJoin(sender: Player, leader: String) =
            this.partyModule.partyFactory.addMember(sender, leader)

    @Subcommand("wyrzuc")
    @Syntax("<nick>")
    fun onKick(sender: Player, kickName: String) =
            this.partyModule.partyFactory.kickPlayerFromParty(sender, kickName)

    @Subcommand("zapros")
    @Syntax("<nick>")
    fun onInvite(sender: Player, invited: String) =
            this.partyModule.partyFactory.invitePlayer(sender, invited)

    @Subcommand("info")
    fun onInfo(sender: Player) =
            PartyGui.getInventory(partyModule).open(sender)

}