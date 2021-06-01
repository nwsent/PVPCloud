package pl.pvpcloud.grouptp.hub.managers

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.hotbar.HotBarItem
import java.util.concurrent.ConcurrentHashMap

class HotBarManager(private val plugin: GroupTpPlugin) {

    private val items = ConcurrentHashMap<HotBarItem, ItemStack>()

    init {
        this.items[HotBarItem.SHOP] = ItemBuilder(Material.EMERALD, 1, "&8* &eSklep &8*").build()
        this.items[HotBarItem.SAVE_INVENTORY] = ItemBuilder(Material.BOOK, 1, "&8* &2Zapisz ewipunek &8*").build()
    }

    fun getLayout(player: Player): Array<ItemStack?> {
        val itemsReturn = arrayOfNulls<ItemStack>(9)
        itemsReturn[0] = this.items[HotBarItem.SAVE_INVENTORY]!!
        itemsReturn[4] = this.items[HotBarItem.SHOP]!!
        itemsReturn[7] = AddonsAPI.HEAD(player.name)
        itemsReturn[8] = AddonsAPI.HUB
        return itemsReturn
    }

    fun getHotBar(itemStack: ItemStack): HotBarItem? {
        this.items.forEach { item ->
            if (item.value == itemStack) {
                return item.key
            }
        }
        return null
    }

}