package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.tools.ToolsPlugin

class HubCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("lobby|hub")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    fun onCommand(sender: Player, @Optional @Flags("other") other: Player?) {
        val target = other ?: sender

        val player = ConnectAPI.getPlayerByUUID(target.uniqueId)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)
        if (player.serverId == "moles_1") {
            player.connect("moles_hub")
        }

        if (player.serverId == "lobby") {
            target.teleport(Bukkit.getWorlds()[0].spawnLocation)
        } else {
            player.connect("lobby")
        }

        sender.sendFixedMessage(" &7» &fTeleportowany na&8: &eHub'a")
    }
}