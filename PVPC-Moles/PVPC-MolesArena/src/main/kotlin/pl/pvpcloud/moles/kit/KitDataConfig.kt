package pl.pvpcloud.moles.kit

import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.helpers.InventorySerializerHelper

data class KitDataConfig(
        val name: String,
        val inventory: String,
        val armor: String
) {

    fun getInventory(): Array<ItemStack> {
        return InventorySerializerHelper.deserializeInventory(this.inventory)
    }

    fun getArmor(): Array<ItemStack> {
        return InventorySerializerHelper.deserializeInventory(this.armor)
    }
}