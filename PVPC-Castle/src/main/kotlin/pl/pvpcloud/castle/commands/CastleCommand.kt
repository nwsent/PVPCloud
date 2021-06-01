package pl.pvpcloud.castle.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.config.CastleConfig
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.save.SaveConfig
import pl.pvpcloud.shop.ShopModule

@CommandAlias("castle")
@CommandPermission("castle.command.castle")
class CastleCommand(private val plugin: CastlePlugin) : BaseCommand() {

    @Subcommand("reload")
    fun onReload(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, CastleConfig::class, "config")
        this.plugin.arenaManager.loadArenaProfiles()
        this.plugin.saveModule.saveConfig = ConfigLoader.load(this.plugin.dataFolder, SaveConfig::class, "saveSections")
        this.plugin.shopModule = ShopModule(this.plugin)
        sender.sendFixedMessage("&ePrzeladowano config")
    }
}