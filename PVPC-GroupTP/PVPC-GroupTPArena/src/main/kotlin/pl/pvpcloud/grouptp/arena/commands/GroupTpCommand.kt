package pl.pvpcloud.grouptp.arena.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import pl.pvpcloud.grouptp.arena.config.GroupTpConfig
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.grouptp.arena.GroupTpPlugin

@CommandAlias("grouptp")
@CommandPermission("grouptp.command.grouptp")
class GroupTpCommand(private val plugin: GroupTpPlugin) : BaseCommand() {

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, GroupTpConfig::class, "config")
        sender.sendFixedMessage("&ePrzeladowano config")
    }
}