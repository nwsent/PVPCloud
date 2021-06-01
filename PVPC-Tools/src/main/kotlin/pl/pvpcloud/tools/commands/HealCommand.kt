package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class HealCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("heal")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.heal")
    fun onCommand(sender: Player, @Optional @Flags("other") other: Player?) {
        val target = other ?: sender
        target.health = 20.0
        target.fireTicks = 0
        target.foodLevel = 20

        target.activePotionEffects.forEach { target.removePotionEffect(it.type) }

        sender.sendFixedMessage(" &7» &fUleczyles gracza&8: &e${target.name}&c.")
    }

}