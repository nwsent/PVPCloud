package pl.pvpcloud.grouptp.hub.save

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class SaveSelectKit(private val plugin: GroupTpPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: GroupTpPlugin): SmartInventory = SmartInventory.builder()
                .id("saveInvSelectKit")
                .provider(SaveSelectKit(plugin))
                .size(4, 9)
                .title("&cWybierz kit".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))
        this.plugin.kitManager.kits.forEach {
            contents.add(ClickableItem.of(it.getInventory()[0]) { _ ->
                SaveGui.getInventory(this.plugin, it).open(player)
            })
        }
    }


    override fun update(player: Player?, contents: InventoryContents?) {}
}