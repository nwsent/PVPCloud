package pl.pvpcloud.hub.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.hub.HubPlugin

@CommandAlias("mode")
@CommandPermission("hub.command.mode")
class ModeCommand(private val plugin: HubPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/mode on <tryb>",
                "&8* &c/mode off <tryb>",
                "&8* &c/mode info <tryb>"
        ))
    }

    @Subcommand("on")
    fun onOn(sender: CommandSender, searchMode: String) {
        val mode = this.plugin.modeManager.getMode(searchMode)
                ?: return sender.sendFixedMessage("&cNie ma takiego trybu")
        mode.isAvailable = true
        sender.sendFixedMessage("&aTryb został włączony")
    }

    @Subcommand("off")
    fun onOff(sender: CommandSender, searchMode: String) {
        val mode = this.plugin.modeManager.getMode(searchMode)
                ?: return sender.sendFixedMessage("&cNie ma takiego trybu")
        mode.isAvailable = false
        sender.sendFixedMessage("&aTryb został włączony")
    }

    @Subcommand("info")
    fun onInfo(sender: CommandSender, searchMode: String) {
        val mode = this.plugin.modeManager.getMode(searchMode)
                ?: return sender.sendFixedMessage("&cNie ma takiego trybu")
        sender.sendFixedMessage("&aStatus: ${mode.isAvailable}")
    }
}