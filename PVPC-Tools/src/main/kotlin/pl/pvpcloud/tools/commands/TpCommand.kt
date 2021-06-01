package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerTeleport

class TpCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("tp")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.teleport")
    fun onTeleportCommand(sender: Player, gracz: String) {
        val playerCache = ConnectAPI.getPlayerByNick(gracz)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val senderCache = ConnectAPI.getPlayerByUUID(sender.uniqueId)
                ?: return
        if (senderCache.serverId != playerCache.serverId) {
            senderCache.connect(playerCache.serverId)

            Bukkit.getScheduler().runTaskLater(this.plugin, {
                NetworkAPI.publish(playerCache.serverId) {
                    PacketPlayerTeleport(sender.uniqueId, playerCache.uuid)
                }
            }, 10)
        } else {
            NetworkAPI.publish(playerCache.serverId) {
                PacketPlayerTeleport(sender.uniqueId, playerCache.uuid)
            }
        }

        sender.sendFixedMessage("&7» &fZostales przeteleportowany do gracza&8: &e$gracz")
    }

    @CommandAlias("stp|tphere")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.teleport")
    fun onTeleportHereCommand(sender: Player, gracz: String) {
        val playerCache = ConnectAPI.getPlayerByNick(gracz)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)

        val senderCache = ConnectAPI.getPlayerByUUID(sender.uniqueId)
                ?: return
        if (senderCache.serverId != playerCache.serverId) {
            sender.sendFixedMessage("&4Ups! &cGracz jest na innym serwerze")
            return
        } else {
            val target = Bukkit.getPlayer(gracz)
                    ?: return
            target.teleport(sender)

            target.sendFixedMessage("&7» &fZostales przeteleportowany do gracza&8: &e$gracz")
        }
    }
}