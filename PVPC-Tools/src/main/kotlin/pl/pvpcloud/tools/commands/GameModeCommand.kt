package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerGameMode

class GameModeCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("gm|gamemode")
    @Syntax("<0/1/2/3> <gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.gamemode")
    fun onGameModeCommand(sender: Player, mode: Int, @Optional @Flags("other") nick: String?) {
        if (mode !in 0..3)
            return sender.sendFixedMessage("&4Ups! &fNie znaleziono takiego trybu gry.")


        val target = (if (nick == null) ConnectAPI.getPlayerByUUID(sender.uniqueId) else ConnectAPI.getPlayerByNick(nick))
                ?: return sender.sendFixedMessage(" &7» &fGracz&8: &4${nick ?: sender.name} &fnie zostal znaleziony w bazie danych.")

        NetworkAPI.publish(target.serverId) {
            PacketPlayerGameMode(target.uuid, mode)
        }
    }

}