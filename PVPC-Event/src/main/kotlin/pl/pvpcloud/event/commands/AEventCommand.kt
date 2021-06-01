package pl.pvpcloud.event.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.config.EventConfig
import pl.pvpcloud.event.events.EventState
import pl.pvpcloud.event.events.EventType
import pl.pvpcloud.event.events.cage.CageEvent
import pl.pvpcloud.event.events.sumo.SumoEvent

@CommandAlias("aevent")
@CommandPermission("aevent.manage")
class AEventCommand(private val plugin: EventPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) =
            sender.sendFixedMessage(arrayListOf(
                    "&8* &c/aevent config",
                    "&8* &c/aevent kit - Zmiana kitu",
                    "&8* &c/aevent create <typ> - Odpala klatki z tym setem co masz na sobie i z eq"
            ))

    @Subcommand("config")
    fun onCommandConfig(sender: CommandSender) {
        this.plugin.config = ConfigLoader.load(this.plugin.dataFolder, EventConfig::class, "config")
        sender.sendFixedMessage("&ePrzeladowano config")
    }

    @Subcommand("create")
    @Syntax("<nazwa> <ile>")
    fun onCommandCreate(sender: Player, nazwa: String, type: String) {
        when (nazwa) {
            "klatki" -> {
                val event = CageEvent(plugin, EventType.valueOf(type))
                event.inventory = sender.inventory.contents
                event.armor = sender.inventory.armorContents
                this.plugin.eventManager.activeEvent = event
                sender.sendFixedMessage("&eKlatki stworzono...")
            }
            "sumo" -> {
                val event = SumoEvent(this.plugin, EventType.valueOf(type))
                event.inventory = sender.inventory.contents
                event.armor = sender.inventory.armorContents
                this.plugin.eventManager.activeEvent = event
                sender.sendFixedMessage("&eSumo stworzono...")
            }
            else -> {
                sender.sendFixedMessage("&cNie ma takiego eventu")
            }
        }
    }

    @Subcommand("start")
    fun onCommandStart(sender: Player) {
        val event = this.plugin.eventManager.activeEvent
                ?: return sender.sendFixedMessage("&eBrak eventu")
        event.start()
        sender.sendFixedMessage("&eEvent startuje...")
    }

    @Subcommand("spawn")
    fun onCommandSpawn(sender: Player) {
        this.plugin.config.spawnLocation = LocationHelper.fromBukkitLocation(sender.location)
        sender.sendFixedMessage("&eUstawiono spawn")
    }

    @Subcommand("kit")
    fun onCommandKit(sender: Player) {
        val event = this.plugin.eventManager.activeEvent
                ?: return sender.sendFixedMessage("&eBrak eventu")
        sender.updateInventory()
        event.inventory = sender.inventory.contents
        event.armor = sender.inventory.armorContents
        sender.sendFixedMessage("&eZmieniłes kit")
    }
}