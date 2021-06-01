package pl.pvpcloud.moles.managers

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.hotbar.HotbarItem
import pl.pvpcloud.moles.hotbar.HotbarLayout
import pl.pvpcloud.moles.hotbar.HotbarLayout.*
import java.util.concurrent.ConcurrentHashMap

class HotbarManager(private val plugin: MolesPlugin) {

    private val items = ConcurrentHashMap<HotbarItem, ItemStack>()

    init {
        this.items[HotbarItem.QUEUE_JOIN] = ItemBuilder(Material.DIAMOND_PICKAXE, 1, "&8* &eDołącz do kolejki &8*").build()
        this.items[HotbarItem.QUEUE_LEAVE] = ItemBuilder(Material.REDSTONE, 1, "&8* &cOpusc kolejke &8*").build()
        this.items[HotbarItem.SHOP] = ItemBuilder(Material.EMERALD, 1, "&8* &eSklep &8*").build()
        this.items[HotbarItem.SPECTATOR_TELEPORTER] = ItemBuilder(Material.COMPASS, 1, "&8* &7Menu obserwacji &8*").build()
        this.items[HotbarItem.PARTY_CREATE] = ItemBuilder(Material.NAME_TAG, "&8* &dZałóż party &8*").build()
        this.items[HotbarItem.PARTY_INFO] = ItemBuilder(Material.PAPER, "&8* &dZobacz party &8*").build()
    }

    fun getLayout(layout: HotbarLayout, player: Player): Array<ItemStack?> {
        val itemsReturn = arrayOfNulls<ItemStack>(9)
        when (layout) {
            LOBBY -> {
                itemsReturn[0] = this.items[HotbarItem.QUEUE_JOIN]!!
              /*  if (this.plugin.partyManager.getParty(player.uniqueId) != null) {
                    itemsReturn[1] = this.items[HotbarItem.PARTY_INFO]!!
                } else {
                    itemsReturn[1] = this.items[HotbarItem.PARTY_CREATE]!!
                }  */
                itemsReturn[4] = this.items[HotbarItem.SHOP]!!
                itemsReturn[7] = AddonsAPI.HEAD(player.name)
                itemsReturn[8] = AddonsAPI.HUB
            }
            QUEUE ->
                itemsReturn[4] = this.items[HotbarItem.QUEUE_LEAVE]!!
            MATCH_SPECTATE ->
                itemsReturn[4] = this.items[HotbarItem.SPECTATOR_TELEPORTER]!!
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