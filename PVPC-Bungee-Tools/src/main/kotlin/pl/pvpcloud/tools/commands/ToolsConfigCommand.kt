package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.config.ToolsConfig

class ToolsConfigCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("btoolsconfig")
    @CommandPermission("tools.command.btoolsconfig")
    fun onToolsConfigCommand(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, ToolsConfig::class, "config")
        sender.sendFixedMessage(plugin.config.messages.reloadMessage)
    }
}