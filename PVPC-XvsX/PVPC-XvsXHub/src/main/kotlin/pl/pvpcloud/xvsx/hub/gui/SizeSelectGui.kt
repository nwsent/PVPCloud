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
import pl.pvpcloud.xvsx.hub.queues.QueueSize

class SizeSelectGui(private val plugin: XvsXPlugin, private val kitName: String) : InventoryProvider {

    companion object {
        fun getInventory(plugin: XvsXPlugin, kitName: String): SmartInventory = SmartInventory.builder()
                .id("sizeSelect")
                .provider(SizeSelectGui(plugin, kitName))
                .size(3, 9)
                .title("&8* &eWybierz typ".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        QueueSize.values().forEach {
            contents.set(it.slot, ClickableItem.of(ItemBuilder(Material.DIAMOND_SWORD, it.size/2, it.itemName).build()) { _ ->
                this.plugin.queueManager.addToQueue(player, this.kitName, it)
            })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}