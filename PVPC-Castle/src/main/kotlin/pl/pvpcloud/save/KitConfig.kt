package pl.pvpcloud.save

import org.bukkit.inventory.ItemStack

data class KitConfig(
        val name: String,
        val version: Int,
        val kitType: KitType,
        val inventory: String,
        val armor: String
) {

    fun getInventory(): Array<ItemStack> {
        return SerializerHelper.deserializeInventory(this.inventory)
    }

    fun getArmor(): Array<ItemStack> {
        return SerializerHelper.deserializeInventory(this.armor)
    }
}