package pl.pvpcloud.addons.profile.casino

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.addons.helpers.InventoryHelper
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.tools.ToolsPlugin

class CasinoGui(private val plugin: ToolsPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: ToolsPlugin): SmartInventory = SmartInventory.builder()
                .id("casinoGui")
                .provider(CasinoGui(plugin))
                .size(3, 9)
                .title(" &8* &eLoteria".fixColors())
                .build()
    }

    override fun update(player: Player, contents: InventoryContents) {
        contents.fillBorder()
        contents.set(1, 4, ClickableItem.of(ItemBuilder(Material.DIAMOND_PICKAXE, "&8* &e50/50 &8*").build()) {
            CasinoFiftyFiftyGui.getInventory(plugin).open(player)
        })
    }

    override fun init(player: Player, contents: InventoryContents) {}

}