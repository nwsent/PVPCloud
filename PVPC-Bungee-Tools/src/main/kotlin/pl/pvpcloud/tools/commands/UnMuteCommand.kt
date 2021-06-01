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

class UnMuteCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("unmute")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.unban")
    fun onCommandUnMute(sender: CommandSender, name: String) {
        val mute = plugin.muteManager.getMute(name)
                ?: return sender.sendFixedMessage(plugin.config.messages.playerIsNotMute.rep("%name%", name))
        plugin.muteManager.removeMute(mute)
        sender.sendFixedMessage(plugin.config.messages.youUnMute.rep("%name%", name))
    }
}