package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalCommand

class GlobalCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("gcmd")
    @Syntax("<komenda>")
    @CommandPermission("tools.command.gcmd")
    fun onGlobalCommand(sender: CommandSender, command: String) {
        NetworkAPI.publish { PacketGlobalCommand(command) }
        sender.sendFixedMessage("&eWyslano komende na wszystkie serwery")
    }
}