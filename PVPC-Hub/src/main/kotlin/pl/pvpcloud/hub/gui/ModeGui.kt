package pl.pvpcloud.hub.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.hub.HubPlugin

class ModeGui(private val plugin: HubPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: HubPlugin): SmartInventory = SmartInventory.builder()
                .id("modeGui")
                .provider(ModeGui(plugin))
                .size(6, 9)
                .title("&8* &eWybierz serwer".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7, " ").build()))
        this.plugin.modeManager.getModes().values.forEach {
            contents.set(it.slot, ClickableItem.of(it.getItem()) { _ ->
                this.plugin.modeManager.connect(player, it)
            })
        }
        contents.set(5, 8, ClickableItem.empty(ItemBuilder(Material.SIGN, 1, "&8* &eInformacje &8*").build()))
    }

    override fun update(player: Player, contents: InventoryContents) {}
}