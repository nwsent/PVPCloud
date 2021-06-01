package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin

class SocialSpyCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("socialspy")
    @CommandPermission("tools.command.socialspy")
    fun onCommand(sender: Player) {
        val user = plugin.userManager.getUserByUUID(sender.uniqueId)
                ?: return sender.sendMessage(this.plugin.config.toolsNotUserInBase)

        user.settings.socialSpy = !user.settings.socialSpy
        sender.sendFixedMessage("&7» &fSocialSpy&8: &e${user.settings.socialSpy}")
    }
}