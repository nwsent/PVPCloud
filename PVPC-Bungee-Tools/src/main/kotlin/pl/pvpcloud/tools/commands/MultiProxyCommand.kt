package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketProxyCommand

class MultiProxyCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("gpcmd")
    @Syntax("<komenda>")
    @CommandPermission("tools.command.gpcmd")
    fun onGlobalCommand(sender: CommandSender, command: String) {
        NetworkAPI.publish { PacketProxyCommand(command) }
        sender.sendFixedMessage("&eWyslano komende na wszystkie serwery")
    }

}