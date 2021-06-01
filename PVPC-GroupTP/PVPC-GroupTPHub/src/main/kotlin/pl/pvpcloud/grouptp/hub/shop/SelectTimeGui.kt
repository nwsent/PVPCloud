package pl.pvpcloud.grouptp.hub.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class SelectTimeGui(private val plugin: GroupTpPlugin, private val shopConfigItem: ShopConfigItem) : InventoryProvider {

    companion object {
        fun getInventory(plugin: GroupTpPlugin, shopConfigItem: ShopConfigItem): SmartInventory = SmartInventory.builder()
                .id("shopGuiTimeSlect")
                .provider(SelectTimeGui(plugin, shopConfigItem))
                .size(5, 9)
                .title("&cWybierz jaki czas".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))
        var i = 2
        contents.set(1,4, ClickableItem.empty(shopConfigItem.itemHelper.toItemStack()))
        TimeType.values().forEach {
            val price: Int = ((shopConfigItem.startPrice * it.days) - (shopConfigItem.startPrice * it.reduction)).toInt()
            contents.set(2, i, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, it.days.toInt(), 14, it.daysName,
                    arrayListOf("", "&8* &7Cena&8: &e$price", "", "&7Kliknij i kup przedmiot!")).build()) { _ ->
                this.plugin.purchasesManager.bayItem(player, this.shopConfigItem, it, price)
                ShopGui.getInventory(this.plugin).open(player)
            })
            i += 2
        }
        contents.set(3, 4, ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
            ShopGui.getInventory(this.plugin).open(player)
        })
    }

    override fun update(player: Player, contents: InventoryContents) {}
}