package pl.pvpcloud.grouptp.hub.shop

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.tools.ToolsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class PurchasesManager(private val plugin: GroupTpPlugin) {

    private val purchasesPlayers = ConcurrentHashMap<UUID, PurchasesPlayer>()

    private val items = ConcurrentHashMap<Int, ItemStack>()

    init {
        DatabaseAPI.loadAll<PurchasesPlayer>("shop-purchases-gtp") { purchasesPlayers ->
            purchasesPlayers.forEach { this.purchasesPlayers[it.uuid] = it }
        }
        this.loadItems()
        this.cleanInvalidPurchase()
    }

    fun getPurchasesPlayer(uuid: UUID): PurchasesPlayer? {
        return this.purchasesPlayers[uuid]
    }

    fun bayItem(player: Player, item: ShopConfigItem, timeType: TimeType, price: Int) {
        val user = ToolsAPI.findUserByUUID(player.uniqueId)
        if (user.coins < price) {
            player.sendFixedMessage("&4* &cNie posiadasz &4$price &ccoinsów")
            return
        }
        user.coins -= price
        var purchasesPlayer = getPurchasesPlayer(player.uniqueId)
        if (purchasesPlayer == null) {
            purchasesPlayer = PurchasesPlayer(player.uniqueId, arrayListOf()).also {
                this.purchasesPlayers[player.uniqueId] = it
                it.insertEntity()
            }
        }
        var purchase = purchasesPlayer.purchases.singleOrNull { it.itemId == item.id }
        if (purchase == null) {
            purchase = Purchase(item.id, System.currentTimeMillis(), TimeUnit.DAYS.toMillis(timeType.days))
            purchasesPlayer.purchases.add(purchase)
        } else {
            if (purchase.time < System.currentTimeMillis()) {
                purchase.time = System.currentTimeMillis()
            }
            purchase.time += TimeUnit.DAYS.toMillis(timeType.days)
        }
        purchasesPlayer.updateEntity()
    }

    fun validShopItems(uniqueId: UUID, inventory: Array<ItemStack>): Array<ItemStack> { //todo do poprawy zeby slot iterator byl
        val purchasesPlayer = this.getPurchasesPlayer(uniqueId)
        val list = inventory.toMutableList()
        if (purchasesPlayer != null) {
            val items = purchasesPlayer.purchasesActive.mapNotNull { this.items[it.itemId] }
            for (shopItem in this.items.values) {
                if (!items.contains(shopItem)) {
                    list.remove(shopItem)
                }
            }
        } else {
            this.items.values.forEach {
                list.remove(it)
            }
        }
        return list.toTypedArray()
    }

    fun addItems(player: Player) {
        ItemsHelper.addItems(player, this.items.values)
    }

    private fun loadItems() {
        this.plugin.shopConfig.shopItems.forEach { shopItem ->
            this.items[shopItem.id] = shopItem.itemHelper.toItemStack()
        }
    }

    private fun cleanInvalidPurchase() {
        this.purchasesPlayers.values.forEach {
            val purchasesPlayer = it
            it.purchases
                    .filter { purchase -> this.items[purchase.itemId] == null }
                    .forEach { purchase ->
                        purchasesPlayer.purchases.remove(purchase)
                        purchasesPlayer.updateEntity()
                    }
        }
    }
}