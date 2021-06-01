package pl.pvpcloud.grouptp.hub.kit

import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.helpers.InventorySerializerHelper

data class Kit(
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