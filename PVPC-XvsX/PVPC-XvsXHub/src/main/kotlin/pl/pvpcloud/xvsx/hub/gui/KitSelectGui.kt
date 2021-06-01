package pl.pvpcloud.xvsx.hub.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.xvsx.hub.XvsXPlugin

class KitSelectGui(private val plugin: XvsXPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: XvsXPlugin): SmartInventory = SmartInventory.builder()
                .id("kitSelect")
                .provider(KitSelectGui(plugin))
                .size(4, 9)
                .title("&8* &eWybierz zestaw".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        this.plugin.commonModule.kitManager.kits.values.forEach {
            contents.set(it.slot, ClickableItem.of(it.iconItem) { _ ->
                SizeSelectGui.getInventory(this.plugin, it.name).open(player)
            })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}