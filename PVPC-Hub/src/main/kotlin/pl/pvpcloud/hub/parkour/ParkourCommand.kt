package pl.pvpcloud.hub.parkour

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.hub.HubPlugin

class ParkourCommand(private val plugin: HubPlugin) : BaseCommand() {

    @CommandAlias("parkour")
    @CommandPermission("parkour.admin")
    fun onCommand(sender: Player) {
      //  plugin.parkourManager.add
       // plugin.parkourConfig.easy.blocks.add(Pair(parkourStage, LocationHelper(sender.location.x, sender.location.y, sender.location.z)))
      //  ConfigLoader.saveInstance(this.plugin.dataFolder, this.plugin.parkourConfig, "parkourConfig")

       // sender.sendFixedMessage("Dodales state: $parkourStage")
       // parkourStage += 1
    }

}