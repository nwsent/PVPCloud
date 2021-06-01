package pl.pvpcloud.shop

import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.ItemsHelper

object ShopAPI {

    internal lateinit var shopModule: ShopModule

    fun openShopInventory(player: Player) {
        ShopGui.getInventory(this.shopModule).open(player)
    }

    fun giveItems(player: Player) {
        val purchasesPlayer = this.shopModule.purchasesManager.getPurchasesPlayer(player.uniqueId)
        if (purchasesPlayer != null) {
            ItemsHelper.addItems(player, purchasesPlayer.purchasesFilter.mapNotNull { this.shopModule.purchasesManager.items[it.itemId] })
        }
    }
}