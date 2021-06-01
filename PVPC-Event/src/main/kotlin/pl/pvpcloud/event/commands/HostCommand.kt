package pl.pvpcloud.event.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.gui.HostEventGui

class HostCommand(private val plugin: EventPlugin) : BaseCommand() {

    @CommandAlias("host")
    @CommandPermission("event.host")
    fun onCommandHost(sender: Player) {
        if (this.plugin.eventManager.activeEvent != null) {
            sender.sendFixedMessage("&cJest już event :O")
            return
        }
        HostEventGui.getInventory(this.plugin).open(sender)
    }




}