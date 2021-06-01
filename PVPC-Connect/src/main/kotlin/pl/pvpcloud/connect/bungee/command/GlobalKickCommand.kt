package pl.pvpcloud.connect.bungee.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.connect.api.structure.PlayerRepository

class GlobalKickCommand : Command("gkick") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("connect.kick")) {
            return sender.sendMessage(TextComponent("Nie masz uprawnien do tej komendy."))
        }

        if (args.size != 1) {
            return sender.sendMessage(TextComponent("Poprawne uzycie: /gkick <nick>."))
        }

        val nick = args[0]

        val player = ProxyServer.getInstance().getPlayer(nick) ?: return sender.sendMessage(TextComponent("Nie znaleziono gracza $nick w bazie danych."))
        player.disconnect(TextComponent("Zostales wyrzucony!"))
    }

}