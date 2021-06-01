package pl.pvpcloud.shop

import fr.minuskube.inv.content.SlotPos
import pl.pvpcloud.common.helpers.ItemEnchantedHelper

data class ShopConfigItem(
        val id: Int,
        val name: String,
        val startPrice: Int,
        val slot: SlotPos,
        val itemHelper: ItemEnchantedHelper
)