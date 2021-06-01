package pl.pvpcloud.ffa.limits

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player

class LimitsCommand(private val limitsModule: LimitsModule) : BaseCommand() {

    @CommandAlias("schowek|depozyt")
    @CommandPermission("ffa.schowek")
    fun onDeposit(sender: Player) {
        LimitsGui.getInventory(limitsModule).open(sender)
    }
}