package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import java.util.regex.Pattern
import java.util.stream.Collectors

@CommandAlias("ignore|wyjebane")
class IgnoreCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @Subcommand("dodaj|add")
    @Syntax("<gracz>")
    fun onCommandAdd(sender: CommandSender, name: String) {
        val user = this.plugin.userManager.getUserByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        if (name.length > 16)
            return sender.sendFixedMessage("&4Ups! &fNick nie ma wiecej niż 16liter")

        val pattern = "^[0-9a-zA-Z-_]+$".toRegex()
        if (!name.matches(pattern))
            return sender.sendFixedMessage("&4Ups! &fGracz ma potecjalnie dziwny nick")

        if (name in user.settings.ignorePlayers) {
            user.settings.ignorePlayers.remove(name)
            user.updateEntity()
            sender.sendFixedMessage(" &7» &fGracz&8: &e${name} &fjuż może do ciebie pisać")
            return
        }

        if (user.settings.ignorePlayers.size > 9)
            return sender.sendFixedMessage("&4Ups! &fMozesz mieć max 10osob")

        user.settings.ignorePlayers.add(name)
        user.updateEntity()
        sender.sendFixedMessage(" &7» &fGracz&8: &e${name} &fjuż nie może do ciebie pisać")
    }

    @Subcommand("wszystkich|all")
    fun onCommandAdd(sender: CommandSender) {
        val user = this.plugin.userManager.getUserByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        user.settings.igonorePrivateMessages = !user.settings.igonorePrivateMessages
        user.updateEntity()
        sender.sendFixedMessage(" &7» &fPisanie do ciebie jest: ${if (!user.settings.igonorePrivateMessages) "&aMożliwe" else "&cnie możliwe"}")
    }

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        val user = this.plugin.userManager.getUserByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val userIgnores = user.settings.ignorePlayers.asSequence().joinToString("") { "$it&8, &f" }

        sender.sendFixedMessage(arrayListOf(
               " &8)&m-------------&8&l|&7»  &eIgnoracja  &7«&8&l|&8&m-------------&r&8(",
               " ",
               " &7/ignore dodaj &8<&fnick&8> &8- &fDodaje/usuwa osobe z ignorowanych",
               " &7/ignore all &8- &fWłącza/wyłącza pisanie do ciebie przez wszystkich",
               " ",
               " &8* &7Ignoracje wszystkich graczy: ${if (!user.settings.igonorePrivateMessages) "&aMożliwe" else "&cnie możliwe"}",
               " &8* &7Gracze których ignorujesz: &f${if (user.settings.ignorePlayers.isNotEmpty()) userIgnores else "Brak"}",
               " ",
               " &8)&m-------------&8&l|&7»  &eIgnoracja  &7«&8&l|&8&m-------------&r&8("
       ))
    }

}
