package pl.pvpcloud.addons.profile.shop

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemEnchantedHelper

class ShopListHelper {

    val services = arrayListOf(
            ShopConfigItem(
                    1,
                    "&8* &6VIP &8*",
                    400,
                    SlotPos(1, 1),
                    ItemEnchantedHelper("", Material.DIAMOND_BLOCK.id, 1),
                    "lp user %name% parent addtemp vip %time%"
            ),
            ShopConfigItem(
                    2,
                    "&8* &6ZEUS &8*",
                    600,
                    SlotPos(1, 2),
                    ItemEnchantedHelper("", Material.BED.id, 1),
                    "lp user %name% parent addtemp zeus %time%"
            )
    )

}