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
import pl.pvpcloud.tools.basic.BanType

class BanCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("ban")
    @Syntax("<gracz> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.ban")
    fun onCommandPermId(sender: CommandSender, @Flags("other") name: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        if (plugin.banManager.getBan(name, BanType.PLAYER) != null)
            return sender.sendFixedMessage(plugin.config.messages.playerHasAlreadyBanID.rep("%name%", name))
        val banId = plugin.banManager.banId(name, sender.name, -1, reason)
        sender.sendFixedMessage(banId.replaceString(plugin.config.messages.youPermBanPlayer))
        plugin.banManager.kickPlayerAfterBan(name, banId)
        NetworkAPI.publish { PacketGlobalMessage(banId.replaceString(plugin.config.messages.playerGotPermBan).fixColors(), "tools.helper") }
    }

    /*@CommandAlias("banip")
    @Syntax("<gracz> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.banip")
    fun onCommandBanIP(sender: CommandSender, @Flags("other") name: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        if (plugin.banManager.getBan(name, BanType.IP) != null)
            return sender.sendFixedMessage(plugin.config.messages.playerHasAlreadyBanIP.rep("%name%", name))
        val p = ProxyServer.getInstance().getPlayer(name)
                ?: return sender.sendFixedMessage(plugin.config.messages.playerOffline)
        val banIP = plugin.banManager.banIp(p, sender.name, -1, reason)
        sender.sendFixedMessage(banIP.replaceString(plugin.config.messages.youPermBanPlayer))
        plugin.banManager.kickPlayerAfterBan(name, banIP)

        ProxyServer.getInstance().broadcast(TextComponent(banIP.replaceString(plugin.config.messages.playerGotPermBan).fixColors()))
    }*/

}