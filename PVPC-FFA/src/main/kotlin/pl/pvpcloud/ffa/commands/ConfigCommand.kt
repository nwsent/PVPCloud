package pl.pvpcloud.ffa.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.ffa.config.FFAConfig

class ConfigCommand(private val plugin: FFAPlugin) : BaseCommand() {

    @CommandAlias("ffaconfig")
    @CommandPermission("tools.command.reload")
    fun onCommand(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, FFAConfig::class, "config")
        sender.sendFixedMessage("&8* &eConfig przeładowany")
    }
    
}