package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.command.CommandSender
import pl.pvpcloud.addons.config.AddonsConfig
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.config.ToolsAutoMessageConfig
import pl.pvpcloud.tools.config.ToolsConfig

class ToolsConfigCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("toolsconfig")
    @CommandPermission("tools.command.reload")
    fun onCommand(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, ToolsConfig::class, "config")
        this.plugin.autoMessageConfig = ConfigLoader.load(this.plugin.dataFolder, ToolsAutoMessageConfig::class, "automsg")
        this.plugin.addonsModule.config = ConfigLoader.load(this.plugin.dataFolder, AddonsConfig::class, "addons")
        sender.sendFixedMessage("&7» &aPrzeladowano config!")
    }
}