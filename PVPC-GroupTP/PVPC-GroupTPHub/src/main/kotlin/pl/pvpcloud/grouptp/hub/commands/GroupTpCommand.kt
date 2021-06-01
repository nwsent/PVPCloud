package pl.pvpcloud.grouptp.hub.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.config.GroupTpConfig

@CommandAlias("grouptp")
@CommandPermission("grouptp.command.grouptp")
class GroupTpCommand(private val plugin: GroupTpPlugin) : BaseCommand() {

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, GroupTpConfig::class, "config")
        sender.sendFixedMessage("&ePrzeladowano config")
    }
}