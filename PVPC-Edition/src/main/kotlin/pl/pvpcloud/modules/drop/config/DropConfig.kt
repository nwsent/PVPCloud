package pl.pvpcloud.modules.drop.config

import org.bukkit.Material
import pl.pvpcloud.modules.drop.structure.DropItem

class DropConfig {

    val blockedIds = setOf(16, 15, 14, 21, 56, 73, 129, 153)
    val drops = setOf(
            DropItem(0, "Diamenty", 50, 2, 3, 3, true, Material.DIAMOND),
            DropItem(1, "Zelazo", 50, 5, 1, 3, true, Material.IRON_INGOT),
            DropItem(2, "Zloto", 50, 1, 1, 3, true, Material.GOLD_INGOT),
            DropItem(3, "Wegiel", 50, 7, 1, 3, true, Material.COAL)
    )

}