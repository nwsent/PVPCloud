package pl.pvpcloud.xvsx.hub.managers

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.hotbar.HotBarItem
import pl.pvpcloud.xvsx.hub.hotbar.HotBarLayout
import java.util.concurrent.ConcurrentHashMap

class HotBarManager(private val plugin: XvsXPlugin) {

    private val items = ConcurrentHashMap<HotBarItem, ItemStack>()

    init {
        this.items[HotBarItem.QUEUE_JOIN] = ItemBuilder(Material.DIAMOND_SWORD, 1, "&8* &eDołącz do kolejki &8*").build()
        this.items[HotBarItem.QUEUE_LEAVE] = ItemBuilder(Material.REDSTONE, 1, "&8* &cOpusc kolejke &8*").build()
        this.items[HotBarItem.SAVE_INVENTORY] = ItemBuilder(Material.BOOK, 1, "&8* &2Zapisz ewipunek &8*").build()
        this.items[HotBarItem.PARTY_CREATE] = ItemBuilder(Material.NAME_TAG, "&8* &dZałóż party &8*").build()
        this.items[HotBarItem.PARTY_INFO] = ItemBuilder(Material.PAPER, "&8* &dZobacz party &8*").build()
    }

    fun getLayout(layout: HotBarLayout, player: Player): Array<ItemStack?> {
        val itemsReturn = arrayOfNulls<ItemStack>(9)
        when (layout) {
            HotBarLayout.LOBBY -> {
                itemsReturn[0] = this.items[HotBarItem.QUEUE_JOIN]!!
                if (PartyAPI.getParty(player.uniqueId) != null) {
                    itemsReturn[1] = this.items[HotBarItem.PARTY_INFO]!!
                } else {
                    itemsReturn[1] = this.items[HotBarItem.PARTY_CREATE]!!
                }
                itemsReturn[6] = this.items[HotBarItem.SAVE_INVENTORY]!!
                itemsReturn[7] = AddonsAPI.HEAD(player.name)
                itemsReturn[8] = AddonsAPI.HUB
            }
            HotBarLayout.QUEUE ->
                itemsReturn[4] = this.items[HotBarItem.QUEUE_LEAVE]!!
        }
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