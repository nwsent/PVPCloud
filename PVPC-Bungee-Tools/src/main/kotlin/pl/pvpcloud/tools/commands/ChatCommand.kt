package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketChatClear
import pl.pvpcloud.packets.chat.PacketChatDelay
import pl.pvpcloud.packets.chat.PacketChatSwitchLock
import pl.pvpcloud.tools.ToolsPlugin
import java.util.concurrent.TimeUnit

@CommandAlias("chat")
@CommandPermission("tools.command.chat")
class ChatCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) {
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/chat cc",
                "&8* &c/chat off",
                "&8* &c/chat on",
                "&8* &c/chat delay <czas 1000=1sek>"
        ))
    }

    @Subcommand("off")
    fun onCommandOff(sender: CommandSender) {
        NetworkAPI.publish { PacketChatSwitchLock(true, sender.name) }
        sender.sendFixedMessage(plugin.config.messages.youLockedChat)
    }

    @Subcommand("on")
    fun onCommandOn(sender: CommandSender) {
        NetworkAPI.publish { PacketChatSwitchLock(false, sender.name) }
        sender.sendFixedMessage(plugin.config.messages.youUnlockedChat)
    }

    @Subcommand("delay")
    @Syntax("<opoznienie>")
    fun onCommandDelay(sender: CommandSender, delay: Long) {
        NetworkAPI.publish { PacketChatDelay(delay) }
        sender.sendFixedMessage(plugin.config.messages.youSetDelayChat.rep("%time%", "${TimeUnit.MILLISECONDS.toSeconds(delay)}"))
    }

    @Subcommand("clear|cc")
    fun onCommandClear(sender: CommandSender) {
        NetworkAPI.publish { PacketChatClear(sender.name) }
    }
}