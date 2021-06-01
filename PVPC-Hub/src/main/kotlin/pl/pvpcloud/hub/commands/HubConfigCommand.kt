package pl.pvpcloud.hub.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.hub.HubPlugin
import pl.pvpcloud.hub.config.HubConfig

class HubConfigCommand(private val plugin: HubPlugin) : BaseCommand() {

    @CommandAlias("hubconfig")
    @CommandPermission("hub.command.reload")
    fun onCommand(sender: Player) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, HubConfig::class, "config")
        this.plugin.modeManager.loadModes()
        //plugin.parkourManager.users.remove(sender.uniqueId)
        sender.sendFixedMessage("&8&l» &aPrzeladowano config!")
    }
}