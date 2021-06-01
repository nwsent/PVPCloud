package pl.pvpcloud.ffa.shop

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemEnchantedHelper

class ShopConfig {

    val shopItems = arrayListOf(
            ShopConfigItem(
                    1,
                    "&8* &eLód &8*",
                    200,
                    SlotPos(1, 1),
                    ItemEnchantedHelper("", Material.PACKED_ICE.id, 64)
            )
    )
}