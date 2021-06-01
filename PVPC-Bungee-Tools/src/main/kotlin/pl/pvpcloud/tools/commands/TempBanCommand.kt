package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.BanType

class TempBanCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("tempban")
    @Syntax("<gracz> <czas> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.tempban")
    fun onCommandTempBanID(sender: CommandSender, @Flags("other") name: String, time: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        if (plugin.banManager.getBan(name, BanType.PLAYER) != null) return sender.sendFixedMessage(plugin.config.messages.playerHasAlreadyBanID.rep("%name%", name))
        val banId = plugin.banManager.banId(name, sender.name, DataHelper.parseString(time), reason)
        sender.sendFixedMessage(banId.replaceString(plugin.config.messages.youTempBanPlayer))
        val player = ProxyServer.getInstance().getPlayer(name)
        if (player != null) plugin.banManager.kickPlayerAfterBan(player.name, banId)
        NetworkAPI.publish { PacketGlobalMessage(banId.replaceString(plugin.config.messages.playerGotBan).fixColors(), "tools.helper") }
    }

   /*@CommandAlias("tempbanip")
    @Syntax("<gracz> <czas> <powod>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.tempbanip")
    fun onCommandTempBanIP(sender: CommandSender, @Flags("other") p: ProxiedPlayer, time: String, reason: String) {
        if (name.contains("Natusiek") || name.contains("Krzysiekigry") || name.contains("DaanielZ"))
            return sender.sendFixedMessage("No No No")
        val banIp = plugin.banManager.banIp(p, sender.name, DataHelper.parseString(time), reason)
        sender.sendFixedMessage(banIp.replaceString(plugin.config.messages.youTempBanPlayer))
        plugin.banManager.kickPlayerAfterBan(p.name, banIp)
        ProxyServer.getInstance().broadcast(TextComponent(banIp.replaceString(plugin.config.messages.playerGotBan).fixColors()))
    }*/

}