package pl.pvpcloud.moles.hub.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.config.MolesConfig

@CommandAlias("moles")
@CommandPermission("moles.command.moles")
class MolesCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, MolesConfig::class, "config")
        sender.sendFixedMessage("&ePrzeladowano config")
    }

    @Subcommand("min")
    @Syntax("<liczba>")
    fun onMin(sender: CommandSender, liczba: Int) {
        this.plugin.config.minToStart = liczba
        sender.sendFixedMessage("&eZmieniono min liczbe do startu")
    }
}