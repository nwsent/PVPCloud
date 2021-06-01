package pl.pvpcloud.moles.hub.managers

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.hotbar.HotBarItem
import pl.pvpcloud.moles.hub.hotbar.HotBarLayout
import pl.pvpcloud.moles.hub.hotbar.HotBarLayout.LOBBY
import pl.pvpcloud.moles.hub.hotbar.HotBarLayout.QUEUE
import pl.pvpcloud.party.PartyAPI
import java.util.concurrent.ConcurrentHashMap

class HotbarManager(private val plugin: MolesPlugin) {

    private val items = ConcurrentHashMap<HotBarItem, ItemStack>()

    init {
        this.items[HotBarItem.QUEUE_JOIN] = ItemBuilder(Material.DIAMOND_PICKAXE, 1, "&8* &eDołącz do kolejki &8*").build()
        this.items[HotBarItem.QUEUE_LEAVE] = ItemBuilder(Material.REDSTONE, 1, "&8* &cOpusc kolejke &8*").build()
        this.items[HotBarItem.SHOP] = ItemBuilder(Material.EMERALD, 1, "&8* &eSklep &8*").build()
        this.items[HotBarItem.PARTY_CREATE] = ItemBuilder(Material.NAME_TAG, 1, "&8* &fZałóż party &8*").build()
        this.items[HotBarItem.PARTY_INFO] = ItemBuilder(Material.PAPER, 1,"&8* &fZobacz party &8*").build()
    }

    fun getLayout(layout: HotBarLayout, player: Player): Array<ItemStack?> {
        val itemsReturn = arrayOfNulls<ItemStack>(9)
        when (layout) {
            LOBBY -> {
                itemsReturn[0] = this.items[HotBarItem.QUEUE_JOIN]!!
                if (PartyAPI.getParty(player.uniqueId) != null) {
                    itemsReturn[1] = this.items[HotBarItem.PARTY_INFO]!!
                } else {
                    itemsReturn[1] = this.items[HotBarItem.PARTY_CREATE]!!
                }
                itemsReturn[4] = this.items[HotBarItem.SHOP]!!
                itemsReturn[7] = AddonsAPI.HEAD(player.name)
                itemsReturn[8] = AddonsAPI.HUB
            }
            QUEUE ->
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