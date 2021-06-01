package pl.pvpcloud.ffa.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.ffa.FFAPlugin

class ShopGui(private val plugin: FFAPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: FFAPlugin): SmartInventory = SmartInventory.builder()
                .id("shop")
                .provider(ShopGui(plugin))
                .size(5, 9)
                .title("&8* &eSklep".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))
        val purchasesPlayer = this.plugin.purchasesManager.getPurchasesPlayer(player.uniqueId)
        this.plugin.shopConfig.shopItems.forEach {
            val item = it.itemHelper.toItemStack()
            val itemMeta = item.itemMeta
            itemMeta.displayName = it.name.fixColors()
            if (purchasesPlayer != null) {
                val purchase = purchasesPlayer.purchasesFilter.singleOrNull { purchase -> purchase.itemId == it.id }
                if (purchase == null) {
                    itemMeta.lore = arrayListOf("", "&8* &7Status&8: &cNie kupiłeś tego przedmiotu.".fixColors(), "")
                } else {
                    itemMeta.lore = arrayListOf("", "&8* &7Status&8: &ePrzedmiot kupiony".fixColors(), "&8* &7Wygasa&8: &e${DataHelper.formatData(purchase.expireTime)}".fixColors())
                }
            }
            item.itemMeta = itemMeta
            contents.set(it.slot, ClickableItem.of(item) { _ ->
                SelectTimeGui.getInventory(this.plugin, it).open(player)
            })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}