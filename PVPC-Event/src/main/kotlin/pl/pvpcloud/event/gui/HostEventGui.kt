package pl.pvpcloud.event.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.EventType

class HostEventGui(private val plugin: EventPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: EventPlugin): SmartInventory = SmartInventory.builder()
                .id("hostGui")
                .provider(HostEventGui(plugin))
                .size(3, 9)
                .title(" &8* &eHost Menu".fixColors())
                .build()
    }


    override fun init(player: Player, contents: InventoryContents) {
        var i = 0;
        EventType.values().forEach {
            contents.set(1, i, ClickableItem.of(ItemHelper(it.name, Material.DIAMOND_SWORD.id, 1).toItemStack()) { _ ->
                HostSelectKitGui.getInventory(this.plugin, it).open(player)
            })
            i++
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}