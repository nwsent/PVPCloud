package pl.pvpcloud.xvsx.hub.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CatchUnknown
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.xvsx.hub.XvsXPlugin

@CommandAlias("duel")
class DuelCommand(private val plugin: XvsXPlugin) : BaseCommand() {

    @Subcommand("help|pomoc")
    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) =
            sender.sendFixedMessage(arrayListOf(
                    " ",
                    " &8)&m-------------&8&l|&7»  &eDuel  &7«&8&l|&8&m-------------&r&8(",
                    " ",
                    " Czym jest Duel? Napisac",
                    " ",
                    " &e/duel zapros <nick/lider party> &8- &fWyzywa na pojedynek",
                    " ",
                    " &8)&m-------------&8&l|&7»  &eDuel  &7«&8&l|&8&m-------------&r&8("
            ))

    @Subcommand("zapros")
    fun onInvite(sender: Player, request: String) {
        sender.sendFixedMessage(":O")
    }

}