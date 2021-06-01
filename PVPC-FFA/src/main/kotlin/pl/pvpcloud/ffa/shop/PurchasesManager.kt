package pl.pvpcloud.ffa.shop

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.tools.ToolsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class PurchasesManager(private val plugin: FFAPlugin) {

    private val purchasesPlayers = ConcurrentHashMap<UUID, PurchasesPlayer>()

    val items = ConcurrentHashMap<Int, ItemStack>()

    init {
        DatabaseAPI.loadAll<PurchasesPlayer>("shop-purchases-ffa") { purchasesPlayers ->
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

    fun validShopItems(player: Player) {
        val purchasesPlayer = this.getPurchasesPlayer(player.uniqueId)
        if (purchasesPlayer != null) {
            val items = purchasesPlayer.purchasesFilter.mapNotNull { this.items[it.itemId] }
            for (shopItem in this.items.values) {
                if (!items.contains(shopItem)) {
                    player.inventory.remove(shopItem)
                }
            }
            player.updateInventory()
        } else {
            this.items.values.forEach {
                player.inventory.remove(it)
            }
            player.updateInventory()
        }
    }

    private fun loadItems() {
        this.plugin.shopConfig.shopItems.forEach {
            this.items[it.id] = it.itemHelper.toItemStack()
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