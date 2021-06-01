package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

@CommandAlias("whitelist|wl")
@CommandPermission("tools.command.whitelist")
class WhiteListCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
            "&8* &c/wl on",
            "&8* &c/wl off",
            "&8* &c/wl list",
            "&8* &c/wl add <nick>",
            "&8* &c/wl remove <nick>",
            "&8* &c/wl reason <powód>"
        ))
    }

    @Subcommand("on")
    fun onEnableWhiteList(sender: CommandSender) {
        this.plugin.config.whiteListStatus = true
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.whiteListOn)
    }

    @Subcommand("off")
    fun onDisableWhiteList(sender: CommandSender) {
        this.plugin.config.whiteListStatus = false
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.whiteListOff)
    }

    @Subcommand("add")
    fun onAddWhiteList(sender: CommandSender, name: String) {
        this.plugin.config.whiteListPlayers.add(name.toLowerCase())
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.whiteListAdd.rep("%name%", name))
    }

    @Subcommand("remove")
    fun onRemoveWhiteList(sender: CommandSender, name: String) {
        this.plugin.config.whiteListPlayers.remove(name.toLowerCase())
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.whiteListRemove.rep("%name%", name))
    }

    @Subcommand("reason")
    fun onReasonWhiteList(sender: CommandSender, reason: String) {
        this.plugin.config.whiteListReason = reason
        this.plugin.saveConfig()
        this.plugin.proxySync()
        sender.sendFixedMessage(plugin.config.messages.whiteListReason.rep("%reason%", reason))
    }

    @Subcommand("list")
    fun onListWhiteList(sender: CommandSender) {
        val message = plugin.config.whiteListPlayers
        sender.sendFixedMessage(plugin.config.messages.whiteListList.rep("%players%", message.toString()))
    }
}