package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class FlyCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("fly")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.fly")
    fun onFlyCommand(sender: Player, @Optional @Flags("other") other: Player?) {
        val target = other ?: sender
        val status = !target.isFlying
        target.allowFlight = status
        target.isFlying = status
        sender.sendFixedMessage(" &7» &fTwoj tryb latania zostal zmieniony na&7: ${if (status) "&awlaczony" else "&cwylaczony"}")
    }

}