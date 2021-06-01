package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.Blacklist

class BlackListCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("blacklist")
    @Syntax("<gracz> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.blacklist")
    fun onCommandBanIP(sender: CommandSender, @Flags("other") name: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        if (plugin.blacklistManager.getBlacklistName(name) != null)
            return sender.sendFixedMessage(plugin.config.messages.playerHasAlreadyBlackList.rep("%name%", name))
        val playerCache = ConnectAPI.getPlayerByNick(name)
        val blacklist: Blacklist?
        if (playerCache == null) {
            blacklist = Blacklist(name, ByteArray(0), "-", reason, sender.name)
        } else {
            blacklist = Blacklist(name, playerCache.computerId!!, playerCache.ip!!, reason, sender.name)
            val message = arrayListOf(*plugin.config.messages.formatLoginDisallowBlackList.toTypedArray())
            message.replaceAll { blacklist.replaceString(it) }
            playerCache.kick(message.joinToString(separator = "\n").fixColors())
        }
        this.plugin.blacklistManager.createBlacklist(blacklist)
        sender.sendFixedMessage(blacklist.replaceString(plugin.config.messages.youBlackListPlayer))
        NetworkAPI.publish { PacketGlobalMessage(blacklist.replaceString(plugin.config.messages.playerGotBlackList).fixColors(), "tools.helper") }
    }
}