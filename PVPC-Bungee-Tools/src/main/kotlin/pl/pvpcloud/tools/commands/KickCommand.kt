package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin

class KickCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("kick")
    @Syntax("<gracz> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.kick")
    fun onCommand(sender: CommandSender, @Flags("other") other: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        val player = ConnectAPI.getPlayerByNick(other)
                ?: return sender.sendFixedMessage(this.plugin.config.messages.playerOffline)
        player.kick(plugin.config.messages.youGotKicked.joinToString("\n")
                .rep("%reason%", reason)
                .rep("%source%", sender.name).fixColors())
        val message = plugin.config.messages.playerGotKicked
                .rep("%name%", other)
                .rep("%reason%", reason)
                .rep("%source%", sender.name)
        NetworkAPI.publish { PacketGlobalMessage(message.fixColors(), "tools.helper") }
    }
}
