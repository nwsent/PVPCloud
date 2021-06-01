package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Syntax
import org.apache.commons.lang.RandomStringUtils
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.User
import java.nio.charset.Charset
import java.util.*


class DiscordCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    /*
    @CommandAlias("discord|dc")
    @Syntax("<discordId>")
    fun onDiscordCommand(sender: Player, other: String) {
        val user = ToolsAPI.findUserByUUID(sender)
        if (user.discordId != -1L) {
            return sender.sendFixedMessage(" &7» &cMasz już ustawione ID zgłoś się do admina po zmiane")
        }
        val id = other.toLongOrNull()
                ?: return sender.sendFixedMessage(" &7» &cTo nie jest discordId")
        user.discordId = id
        user.updateEntity()
        sender.sendFixedMessage(" &7» &fDo twojego konta zostało przypisane id&7: &e$id")
    }

    @CommandAlias("adiscord")
    @Syntax("<gracz> <discordId>")
    @CommandPermission("tools.command.adiscord")
    fun onADiscordCommand(sender: Player, @Flags("other") other: String, id: String) {
        val user = ToolsAPI.getUserByNick(other)
                ?: return sender.sendFixedMessage(" &7» &cNie ma go w bazie")
        val vId = id.toLongOrNull()
                ?: return sender.sendFixedMessage(" &7» &cTo nie jest discordId")
        user.discordId = vId
        user.updateEntity()
        sender.sendFixedMessage(" &7» &fDo gracza &e$other &fzostało przypisane id&7:$id")
    }

     */

    @CommandAlias("discord")
    @CommandPermission("tools.command.adiscord")
    fun onDiscordCommand(sender: Player) {
        val user = ToolsAPI.getUserByNick(sender.name)
                ?: return

        if (user.discord)
            return sender.sendFixedMessage("&4Ups! &fMasz już połączonę konto z discordem")

        if (user.discordKey != "Brak")
            return sender.sendFixedMessage(
                    arrayListOf(
                            " ",
                            " &8* &eAby połączyć swoje konto z discordem &8(&f&odc.pvpcloud.pl&8)",
                            "   &7» &fNapisz do bota komende: &f!polacz ${user.discordKey}",
                            " "
                    ))

        randomKey(user)
        sender.sendFixedMessage(arrayListOf(
                " ",
                " &8* &eAby połączyć swoje konto z discordem &8(&f&odc.pvpcloud.pl&8)",
                "   &7» &fNapisz do bota komende: &f!polacz ${user.discordKey}",
                " "
        ))
    }

    private fun randomKey(user: User) {
        val key = RandomStringUtils.randomAlphabetic(8)
        user.discordKey = key
        user.updateEntity()
    }

}