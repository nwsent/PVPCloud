package pl.pvpcloud.xvsx.common.kit

import fr.minuskube.inv.content.SlotPos
import org.bukkit.inventory.ItemStack

class Kit(
        val name: String,
        val slot: SlotPos,
        val iconItem: ItemStack,
        val kitEquipment: KitEquipment,
        val gameSettings: KitSettings
)