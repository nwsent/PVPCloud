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
import pl.pvpcloud.tools.basic.BanType

class UnBanCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("unban")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.unban")
    fun onCommandUnban(sender: CommandSender, name: String) {
        val ban = plugin.banManager.getBan(name, BanType.PLAYER)
                ?: return sender.sendFixedMessage(plugin.config.messages.playerIsNotBanned.rep("%name%", name))
        plugin.banManager.removeBan(ban)
        sender.sendFixedMessage(plugin.config.messages.youUnbannedId.rep("%name%", name))
    }

    /*@CommandAlias("unbanip")
    @Syntax("<ip>")
    @CommandPermission("tools.command.unbanip")
    fun onCommandUnbanIp(sender: CommandSender, string: String) {
        val ban = plugin.banManager.getBan(string, BanType.IP)
                ?: return sender.sendFixedMessage(plugin.config.messages.ipIsNotBanned.rep("%ip%", string))
        plugin.banManager.removeBan(ban)
        sender.sendFixedMessage(plugin.config.messages.youUnbannedIp.rep("%ip%", string))
    }*/
}