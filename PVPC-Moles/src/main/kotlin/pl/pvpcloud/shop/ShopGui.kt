package pl.pvpcloud.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.DataHelper

class ShopGui(private val shopModule: ShopModule) : InventoryProvider {

    companion object {
        fun getInventory(shopModule: ShopModule): SmartInventory = SmartInventory.builder()
                .id("shop")
                .provider(ShopGui(shopModule))
                .size(4, 9)
                .title("&8* &eSklep".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val purchasesPlayer = ShopAPI.shopModule.purchasesManager.getPurchasesPlayer(player.uniqueId)
        this.shopModule.shopConfig.shopItems.forEach {
            var purchase: Purchase? = null
            if (purchasesPlayer != null) {
                purchase = purchasesPlayer.purchasesFilter.singleOrNull { purchase -> purchase.itemId == it.id }
            }
            val item = it.itemHelper.toItemStack()
            val itemMeta = item.itemMeta
            itemMeta.displayName = it.name.fixColors()
            if (purchase == null) {
                itemMeta.lore = arrayListOf("", "&8* &7Status&8: &cNie kupiłeś tego przedmiotu.".fixColors(), "")
            } else {
                itemMeta.lore = arrayListOf("", "&8* &7Status&8: &ePrzedmiot kupiony".fixColors(), "&8* &7Wygasa&8: &e${DataHelper.formatData(purchase.expireTime)}".fixColors())
            }
            item.itemMeta = itemMeta
            contents.set(it.slot, ClickableItem.of(item) { _ ->
                SelectTimeGui.getInventory(it).open(player)
            })
        }
        contents.set(4, 3, ClickableItem.empty(ItemBuilder(Material.BOOK, 1, "&8* &eKupione &8*").build()))
        contents.set(4, 5, ClickableItem.empty(ItemBuilder(Material.GHAST_TEAR, 1, "&8* &eStan konta &8*").build()))
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}