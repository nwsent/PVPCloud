package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin

class MuteCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("mute")
    @Syntax("<gracz> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.mute")
    fun onMute(sender: CommandSender, @Flags("other") name: String, reason: String) {
        if (this.plugin.muteManager.getMute(name) != null)
            return sender.sendFixedMessage(this.plugin.config.messages.playerHasAlreadyMute.rep("%name%", name))
        val mute = this.plugin.muteManager.mutePlayer(name, sender.name, -1, reason)
        sender.sendFixedMessage(this.plugin.config.messages.youMutePlayer.rep("%name%", name))
        NetworkAPI.publish { PacketGlobalMessage(mute.replaceString(this.plugin.config.messages.playerGotMute).fixColors(), "-") }
    }
}