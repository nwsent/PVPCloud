package pl.pvpcloud.castle.managers

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.hotbar.HotbarItem
import pl.pvpcloud.castle.hotbar.HotbarLayout
import pl.pvpcloud.common.builders.ItemBuilder
import java.util.concurrent.ConcurrentHashMap

class HotbarManager(private val plugin: CastlePlugin) {

    private val items = ConcurrentHashMap<HotbarItem, ItemStack>()

    init {
        this.items[HotbarItem.QUEUE_JOIN] = ItemBuilder(Material.DIAMOND_SWORD, 1, "&8* &cDołącz do kolejki &8*").build()
        this.items[HotbarItem.QUEUE_LEAVE] = ItemBuilder(Material.REDSTONE, 1, "&8* &cOpusc kolejke &8*").build()
        this.items[HotbarItem.QUEUE_PREFERENCES] = ItemBuilder(Material.CHEST, 1, "&8* &aZaglosuj &8*").build()
        this.items[HotbarItem.SAVE_INVENTORY] = ItemBuilder(Material.BOOK, 1, "&8* &2Zapisz ewipunek &8*").build()
        this.items[HotbarItem.SPECTATOR] = ItemBuilder(Material.COMPASS, 1, "&8* &7Obserwuj mecze &8*").build()
        this.items[HotbarItem.SHOP] = ItemBuilder(Material.EMERALD, 1, "&8* &aSklep &8*").build()
    }

    fun getLayout(layout: HotbarLayout, player: Player): Array<ItemStack?> {
        val itemsReturn = arrayOfNulls<ItemStack>(9)
        when (layout) {
            HotbarLayout.LOBBY -> {
                itemsReturn[0] = this.items[HotbarItem.QUEUE_JOIN]!!
                itemsReturn[1] = this.items[HotbarItem.SPECTATOR]!!
                itemsReturn[4] = this.items[HotbarItem.SHOP]!!
                itemsReturn[6] = this.items[HotbarItem.SAVE_INVENTORY]!!
                itemsReturn[7] = AddonsAPI.HEAD(player.name)
                itemsReturn[8] = AddonsAPI.HUB
            }
            HotbarLayout.QUEUE -> {
                itemsReturn[0] = this.items[HotbarItem.QUEUE_LEAVE]!!
                itemsReturn[4] = this.items[HotbarItem.QUEUE_PREFERENCES]!!
            }
        }
        return itemsReturn
    }

    fun getHotBar(itemStack: ItemStack): HotbarItem? {
        this.items.forEach { item ->
            if (item.value == itemStack) {
                return item.key
            }
        }
        return null
    }
}