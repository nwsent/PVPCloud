package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class UnBlacklistCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("unblacklist")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.unblacklist")
    fun onCommandUnban(sender: CommandSender, name: String) {
        val blacklist = plugin.blacklistManager.getBlacklistName(name)
                ?: return sender.sendFixedMessage(plugin.config.messages.playerIsNotBlacklist.rep("%name%", name))
        plugin.blacklistManager.removeBlacklist(blacklist)
        sender.sendFixedMessage(plugin.config.messages.youUnbannedBlacklist.rep("%name%", name))
    }

}