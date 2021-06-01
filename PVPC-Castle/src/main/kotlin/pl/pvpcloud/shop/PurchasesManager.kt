package pl.pvpcloud.shop

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class PurchasesManager(private val shopModule: ShopModule) {

    private val purchasesPlayers = ConcurrentHashMap<UUID, PurchasesPlayer>()

    val items = ConcurrentHashMap<Int, ItemStack>()

    init {
        DatabaseAPI.loadAll<PurchasesPlayer>("shop-purchases-castle") { purchasesPlayers ->
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
        var pPlayer = getPurchasesPlayer(player.uniqueId)
        if (pPlayer == null) {
            pPlayer = PurchasesPlayer(player.uniqueId, arrayListOf()).also {
                this.purchasesPlayers[player.uniqueId] = it
                it.insertEntity()
            }
        }
        if (pPlayer.purchasesFilter.singleOrNull { it.itemId == item.id } == null) {
            val purchase = Purchase(item.id, System.currentTimeMillis(), TimeUnit.DAYS.toMillis(timeType.days))
            pPlayer.purchases.add(purchase)
            pPlayer.updateEntity()
        } else {
            val purchase = pPlayer.purchases.single { it.itemId == item.id }
            purchase.time += TimeUnit.DAYS.toMillis(timeType.days)
            pPlayer.updateEntity()
        }
    }

    private fun loadItems() {
        this.shopModule.shopConfig.shopItems.forEach {
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