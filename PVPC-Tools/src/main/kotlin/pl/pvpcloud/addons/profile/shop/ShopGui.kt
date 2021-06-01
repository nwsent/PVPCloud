package pl.pvpcloud.addons.profile.shop

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class ShopGui(private val shopModule: ShopModule) : InventoryProvider {

    companion object {
        fun getInventory(shopModule: ShopModule): SmartInventory = SmartInventory.builder()
            .id("shopAddonGui")
            .provider(ShopGui(shopModule))
            .size(3, 9)
            .title("&8* &eSklep".fixColors())
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))
        this.shopModule.servicesHelper.services.forEach {
            contents.set(it.slot, ClickableItem.of(it.itemHelper.toItemStack()){

            })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}