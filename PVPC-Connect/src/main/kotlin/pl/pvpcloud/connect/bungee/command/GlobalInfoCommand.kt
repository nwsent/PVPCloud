package pl.pvpcloud.connect.bungee.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.connect.api.structure.PlayerRepository

class GlobalInfoCommand(private val playerRepository: PlayerRepository) : Command("ginfo") { // TODO kolorki dorobic, jak zrobi sie PVPC-Bungee-Common

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("connect.info")) {
            return sender.sendMessage(TextComponent("Nie masz uprawnien do tej komendy."))
        }

        if (args.size != 1) {
            return sender.sendMessage(TextComponent("Poprawne uzycie: /ginfo <nick>."))
        }

        val player = this.playerRepository.getPlayerByNick(args[0])
                ?: return sender.sendMessage(TextComponent("Nie znaleziono gracza w bazie danych."))

        sender.sendMessage(TextComponent("Informacje o ${player.nick}"))
        sender.sendMessage(TextComponent("UUID: ${player.uuid}"))
        sender.sendMessage(TextComponent("Server id: ${player.serverId}"))
        sender.sendMessage(TextComponent("Proxy id: ${player.proxyId}"))
        sender.sendMessage(TextComponent("Ip: ${player.ip}"))
        sender.sendMessage(TextComponent("ComputerId: ${player.computerId?.contentToString()}"))
    }

}