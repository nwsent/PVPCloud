package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerUpdate
import pl.pvpcloud.tools.packets.PacketSocialSpyMessage

class TellCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("tell|msg")
    @Syntax("<gracz> <wiadomosc>")
    @CommandCompletion("@players")
    fun onCommand(sender: Player, @Flags("other") other: String, messages: String) {
        if (sender.name == other)
            return sender.sendFixedMessage("&4Ups! &fNie mozesz napisac do siebie wiadomosci")

        val receiverCache = ConnectAPI.getPlayerByNick(other)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val receiverUser = ToolsAPI.findUserByUUID(receiverCache.uuid)

        val userBlock = receiverUser.settings.ignorePlayers.singleOrNull { it == sender.name } != null

        if (userBlock && !sender.hasPermission("addons.deignore"))
            return sender.sendFixedMessage("&4Ups! &fGracz ma ciebie zablokowanego")

        if (receiverUser.settings.igonorePrivateMessages && !sender.hasPermission("addons.deignore"))
            return sender.sendFixedMessage("&4Ups! &fGracz ma zablokowone pisanie do niego wiadomosci prywatnej")

        val senderCache = ConnectAPI.getPlayerByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        plugin.userManager.getUserByUUID(sender.uniqueId).also {
            if (it != null) {
                it.settings.lastMessage = other
                NetworkAPI.publish { PacketPlayerUpdate(it) }
            }
        }

        plugin.userManager.getUserByNick(other).also {
            if (it != null) {
                it.settings.lastMessage = sender.name
                NetworkAPI.publish { PacketPlayerUpdate(it) }
            }
        }

        send(senderCache, receiverCache, messages)
    }

    @CommandAlias("reply|r")
    @CommandPermission("tools.command.msg")
    @Syntax("<wiadomosc>")
    fun onCommandReply(sender: Player, messages: String) {
        val senderUser = plugin.userManager.getUserByUUID(sender.uniqueId)
                ?: return

        if (senderUser.settings.lastMessage == null)
            return sender.sendFixedMessage("&4Ups! &fNie masz komu odpowiedziec :(")

        val receiverCache = ConnectAPI.getPlayerByNick(senderUser.settings.lastMessage!!)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)


        val receiverUser = ToolsAPI.findUserByUUID(receiverCache.uuid)

        val userBlock = receiverUser.settings.ignorePlayers.singleOrNull { it == sender.name } != null

        if (userBlock && !sender.hasPermission("addons.deignore"))
            return sender.sendFixedMessage("&4Ups! &fGracz ma ciebie zablokowanego")

        if (receiverUser.settings.igonorePrivateMessages && !sender.hasPermission("addons.deignore"))
            return sender.sendFixedMessage("&4Ups! &fGracz ma zablokowone pisanie do niego wiadomosci prywatnej")

        val senderCache = ConnectAPI.getPlayerByNick(sender.name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        send(senderCache, receiverCache, messages)
    }

    private fun send(sender: pl.pvpcloud.connect.api.structure.Player, receiver: pl.pvpcloud.connect.api.structure.Player, message: String) {
        sender.sendMessage("&eJa &8-> &6${receiver.nick} &8» &f$message".fixColors())
        receiver.sendMessage("&6${sender.nick} &8-> &eJa &8-> &f$message".fixColors())
        NetworkAPI.publish { PacketSocialSpyMessage("&8[&4SS&8] &7${sender.nick} &c-> &7${receiver.nick} &8» &7$message") }
    }

}