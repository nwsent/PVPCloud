package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin

class AdminChatCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("ac")
    @Syntax("<wiadomosc>")
    @CommandPermission("tools.command.ac")
    fun onCommandAdminChat(sender: CommandSender, message: String) =
            NetworkAPI.publish { PacketGlobalMessage(" &4&l&nAdminChat&r &8&l» &7${sender.name}&8: &f&o$message".fixColors(), "tools.command.ac") }

}