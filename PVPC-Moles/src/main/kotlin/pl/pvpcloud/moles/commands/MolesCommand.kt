package pl.pvpcloud.moles.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.config.MolesConfig

@CommandAlias("moles")
@CommandPermission("moles.command.moles")
class MolesCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, MolesConfig::class, "config")
        this.plugin.kitModule.kitManager.loadKits()
        sender.sendFixedMessage("&ePrzeladowano config")
    }
}