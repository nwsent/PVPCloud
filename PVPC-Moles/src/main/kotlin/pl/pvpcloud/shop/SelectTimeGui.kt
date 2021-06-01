package pl.pvpcloud.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class SelectTimeGui(private val shopConfigItem: ShopConfigItem) : InventoryProvider {

    companion object {
        fun getInventory(shopConfigItem: ShopConfigItem): SmartInventory = SmartInventory.builder()
                .id("shopGuiTimeSlect")
                .provider(SelectTimeGui(shopConfigItem))
                .size(3, 9)
                .title("&cWybierz jaki czas".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        var i = 2
        TimeType.values().forEach {
            val price: Int = ((shopConfigItem.startPrice * it.days) - (shopConfigItem.startPrice * it.reduction)).toInt()
            contents.set(1, i, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, it.days.toInt(), 14, it.daysName,
                    arrayListOf("", "&8* &7Cena&8: &e$price", "", "&7Kliknij i kup przedmiot!")).build()) { _ ->
                ShopAPI.shopModule.purchasesManager.bayItem(player, this.shopConfigItem, it, price)
                ShopAPI.openShopInventory(player)
            })
            i += 2
        }
    }

    override fun update(player: Player, contents: InventoryContents) {}
}