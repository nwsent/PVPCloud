package pl.pvpcloud.moles.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.party.event.*

@CommandAlias("party")
@CommandPermission("moles.command.party")
class PartyCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @Subcommand("help|pomoc")
    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
            "&8* &c/party zaloz",
            "&8* &c/party usun",
            "&8* &c/party opusc",
            "&8* &c/party dolacz <nick>",
            "&8* &c/party zapros <nick>",
            "&8* &c/party wyrzuc <nick>"
        ))
    }

    @Subcommand("zaloz")
    fun onCreate(sender: Player) =
        this.plugin.server.pluginManager.callEvent(PartyCreateEvent(sender))

    @Subcommand("usun")
    fun onDelete(sender: Player) =
        this.plugin.server.pluginManager.callEvent(PartyDeleteEvent(sender))

    @Subcommand("opusc")
    fun onLeave(sender: Player) =
        this.plugin.server.pluginManager.callEvent(PartyLeaveEvent(sender, false))

    @Subcommand("dolacz")
    @Syntax("<nick>")
    fun onJoin(sender: Player, leader: String) =
        this.plugin.server.pluginManager.callEvent(PartyJoinEvent(sender, leader))

    @Subcommand("wyrzuc")
    @Syntax("<nick>")
    fun onKick(sender: Player, kickName: String) =
        this.plugin.server.pluginManager.callEvent(PartyKickEvent(sender, kickName))

    @Subcommand("zapros")
    @Syntax("<nick>")
    fun onInvite(sender: Player, invited: String) =
        this.plugin.server.pluginManager.callEvent(PartyInviteEvent(sender, invited))

}