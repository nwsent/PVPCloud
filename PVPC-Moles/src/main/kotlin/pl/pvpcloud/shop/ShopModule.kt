package pl.pvpcloud.shop

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.moles.MolesPlugin

class ShopModule(val plugin: MolesPlugin) {

    var shopConfig: ShopConfig = ConfigLoader.load(this.plugin.dataFolder, ShopConfig::class, "shops")
    var purchasesManager: PurchasesManager = PurchasesManager(this)

    init {
        ShopAPI.shopModule = this
    }
}