package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

@CommandAlias("motd")
@CommandPermission("tools.command.motd")
class MotdCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/motd show",
                "&8* &c/motd line1",
                "&8* &c/motd line2"
        ))
    }

    @Subcommand("show")
    fun onShow(sender: CommandSender) {
        this.plugin.proxySync()
        sender.sendFixedMessage(this.plugin.config.description)
    }

    @Subcommand("line1")
    fun onLine1(sender: CommandSender, line1: String) {
        this.plugin.config.description[0] = line1
        this.plugin.proxySync()
        sender.sendFixedMessage(this.plugin.config.description)
    }

    @Subcommand("line2")
    fun onLine2(sender: CommandSender, line2: String) {
        this.plugin.config.description[1] = line2
        this.plugin.proxySync()
        sender.sendFixedMessage(this.plugin.config.description)
    }
}