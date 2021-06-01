package pl.pvpcloud.moles.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.gui.spectator.SpectatorSelectTeamGui
import pl.pvpcloud.moles.profile.ProfileState

class SpectatorCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @CommandAlias("spec|spectator|specmenu")
    @CommandPermission("moles.command.spectator")
    fun onSpecCommand(sender: Player) {
        val profile = this.plugin.profileManager.getProfile(sender)
        if (!profile.isState(ProfileState.SPECTATING)) return
        val match = this.plugin.spectateManager.getMatch(sender.uniqueId)
                ?: return
        SpectatorSelectTeamGui.getInventory(match).open(sender)
    }

}