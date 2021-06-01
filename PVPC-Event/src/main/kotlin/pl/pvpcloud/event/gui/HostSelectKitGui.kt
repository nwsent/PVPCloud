package pl.pvpcloud.event.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.EventType
import pl.pvpcloud.event.events.cage.CageEvent

class HostSelectKitGui(private val plugin: EventPlugin, private val eventType: EventType) : InventoryProvider {

    companion object {
        fun getInventory(plugin: EventPlugin, eventType: EventType): SmartInventory = SmartInventory.builder()
                .id("hostKitGui")
                .provider(HostSelectKitGui(plugin, eventType))
                .size(3, 9)
                .title(" &8* &eHost Menu".fixColors())
                .build()
    }


    override fun init(player: Player, contents: InventoryContents) {
        var i = 0;
        this.plugin.eventManager.kits.values.forEach {
            contents.set(1, i, ClickableItem.of(it.icon) { _ ->
                val event = CageEvent(plugin, eventType)
                event.inventory = it.inventory
                event.armor = it.armor
                this.plugin.eventManager.activeEvent = event
                player.sendFixedMessage("&eKlatki stworzono...")
                player.closeInventory()
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission settemp event.host false 12h")
            })
            i++
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}