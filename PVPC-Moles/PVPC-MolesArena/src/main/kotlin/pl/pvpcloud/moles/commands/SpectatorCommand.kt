package pl.pvpcloud.moles.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.gui.spectator.SpectatorSelectTeamGui

class SpectatorCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @CommandAlias("spec|spectator|specmenu")
    fun onSpecCommand(sender: Player) {
        if (sender.gameMode == GameMode.SPECTATOR) {
            val match = this.plugin.matchManager.getMatchByWorld(sender.world.uid)
                    ?: return
            SpectatorSelectTeamGui.getInventory(match).open(sender)
        }
    }

}