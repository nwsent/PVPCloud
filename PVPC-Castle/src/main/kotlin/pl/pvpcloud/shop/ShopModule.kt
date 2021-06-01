package pl.pvpcloud.shop

import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.common.configuration.ConfigLoader

class ShopModule(val plugin: CastlePlugin) {

    var shopConfig: ShopConfig = ConfigLoader.load(this.plugin.dataFolder, ShopConfig::class, "shops")
    var purchasesManager: PurchasesManager = PurchasesManager(this)

    init {
        ShopAPI.shopModule = this
    }
}