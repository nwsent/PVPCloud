package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalTitle

class BroadcastCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("broadcast|bc")
    @Syntax("<typ> <wiadomosc>")
    @CommandPermission("tools.command.bc")
    fun onCommand(sender: CommandSender, type: String, message: String) =
            when (type) {
                "title" -> NetworkAPI.publish { PacketGlobalTitle("", message) }
                "chat" -> NetworkAPI.publish { PacketGlobalMessage(message.fixColors(), "-") }
                else -> sender.sendFixedMessage("<title/ chat> <wiadomosc>")
            }


    @CommandAlias("bcs")
    @Syntax("<typ> <wiadomosc>")
    @CommandPermission("tools.command.bc")
    fun onCommandBcs(sender: CommandSender, type: String, message: String) =
            when (type) {
                "title" -> Bukkit.getOnlinePlayers().forEach {
                    it.sendTitle("", message, 20, 40, 10)
                }
                "chat" -> Bukkit.getOnlinePlayers().forEach {
                    it.sendFixedMessage(message)
                }
                else -> sender.sendFixedMessage("<title/ chat> <wiadomosc>")
            }
}