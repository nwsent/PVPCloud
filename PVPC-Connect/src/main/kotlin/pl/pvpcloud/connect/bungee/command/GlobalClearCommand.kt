package pl.pvpcloud.connect.bungee.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.redis.RedisAPI

class GlobalClearCommand(private val playerService: PlayerService) : Command("gclear") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (!sender.hasPermission("connect.clear")) {
            return sender.sendMessage(TextComponent("Nie masz uprawnien do tej komendy."))
        }

        if (args.isEmpty()) {
            for (player in ConnectAPI.playerRepository.playerMap.values) {
                RedisAPI.getCommands {
                    it.hdel("pvpc-connect-players", player.uuid.toString())
                }

                this.playerService.deletePlayer(player.uuid)
            }

            sender.sendMessage(TextComponent("Cala baza danych zostala wyczyszczona."))
        } else {
            val serverId = args[0]

            for (player in ConnectAPI.playerRepository.playerMap.values) {
                if (player.serverId.equals(serverId, true)) {
                    RedisAPI.getCommands {
                        it.hdel("pvpc-connect-players", player.uuid.toString())
                    }

                    this.playerService.deletePlayer(player.uuid)
                }
            }

            sender.sendMessage(TextComponent("Baza danych z serwera $serverId zostala wyczyszczona."))
        }
    }

}