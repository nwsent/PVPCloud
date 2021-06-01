package pl.pvpcloud.shop

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemEnchantedHelper

class ShopConfig {

    val shopItems = arrayListOf(
            ShopConfigItem(
                    1,
                    "&8* &eLód &8*",
                    240,
                    SlotPos(1, 1),
                    ItemEnchantedHelper("", Material.PACKED_ICE.id, 48)
            ),
            ShopConfigItem(
                    2,
                    "&8* &eSlime &8*",
                    280,
                    SlotPos(1, 2),
                    ItemEnchantedHelper("", Material.SLIME_BLOCK.id, 2)
            ),
            ShopConfigItem(
                    3,
                    "&8* &eŚnieżki &8*",
                    240,
                    SlotPos(1, 3),
                    ItemEnchantedHelper("", Material.SNOW_BALL.id, 12)
            ),
            ShopConfigItem(
                    4,
                    "&8* &eWędka &8*",
                    400,
                    SlotPos(1,4),
                    ItemEnchantedHelper("", Material.FISHING_ROD.id, 1, 49)
            ),
            ShopConfigItem(
                    5,
                    "&8* &eStrzały &8*",
                    150,
                    SlotPos(1,5),
                    ItemEnchantedHelper("", Material.ARROW.id, 16)
            ),
            ShopConfigItem(
                    6,
                    "&8* &eRedstone &8*",
                    100,
                    SlotPos(1,6),
                    ItemEnchantedHelper("", Material.REDSTONE.id, 48)
            ),
            ShopConfigItem(
                    6,
                    "&8* &eTNT &8*",
                    240,
                    SlotPos(1,7),
                    ItemEnchantedHelper("", Material.TNT.id, 32)
            ),
            ShopConfigItem(
                    6,
                    "&8* &eWiadro Lawy &8*",
                    300,
                    SlotPos(2,1),
                    ItemEnchantedHelper("", Material.LAVA_BUCKET.id, 1)
            )
    )
}