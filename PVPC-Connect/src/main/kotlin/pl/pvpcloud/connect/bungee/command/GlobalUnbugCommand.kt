package pl.pvpcloud.connect.bungee.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.redis.RedisAPI

class GlobalUnbugCommand(private val playerService: PlayerService, private val playerRepository: PlayerRepository) : Command("gunbug") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("connect.unbug")) {
            return sender.sendMessage(TextComponent("Nie masz uprawnien do tej komendy."))
        }

        if (args.size != 1) {
            return sender.sendMessage(TextComponent("Poprawne uzycie: /gunbug <nick>"))
        }

        val nick = args[0]

        val player = this.playerRepository.getPlayerByNick(nick)
                ?: return sender.sendMessage(TextComponent("Nie znaleziono gracza $nick w bazie danych."))

        this.playerService.deletePlayer(player.uuid)

        sender.sendMessage(TextComponent("Gracz $nick zostal wyczyszczony z bazy danych."))
    }

}