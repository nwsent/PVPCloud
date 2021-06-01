package pl.pvpcloud.castle.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.gui.spectator.SpectatorSelectTeamGui
import pl.pvpcloud.castle.profile.ProfileState

class SpectatorCommand(private val plugin: CastlePlugin) : BaseCommand() {

    @CommandAlias("spec|spectator|specmenu")
    fun onSpecCommand(sender: Player) {
        val profile = this.plugin.profileManager.getProfile(sender)
        if (!profile.isState(ProfileState.SPECTATING)) return
        val match = this.plugin.spectateManager.getMatch(sender.uniqueId)
                ?: return
        SpectatorSelectTeamGui.getInventory(match).open(sender)
    }

}