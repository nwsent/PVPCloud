package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class ClearCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("clearinv|clean|clear")
    @Syntax("<gracz>")
    @CommandCompletion("@players")
    @CommandPermission("tools.command.clearinv")
    fun onCommandClearInv(sender: Player, @Optional @Flags("other") other: Player?) {
        val target = other ?: sender
        target.inventory.clear()
        target.inventory.armorContents[4]
        sender.sendFixedMessage(" &7» &fWyczyszczono ekwipunek graczowi&8: &e${target.name}")
    }

}