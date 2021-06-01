package pl.pvpcloud.addons.profile

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player
import pl.pvpcloud.addons.AddonsModule

class ProfileCommand(private val addonsModule: AddonsModule) : BaseCommand() {

    @CommandAlias("profil|disco|particle|chat|incognito|ustawienia")
    fun onProfileCommand(sender: Player) {
        ProfileGui.getInventory(this.addonsModule).open(sender)
    }

}