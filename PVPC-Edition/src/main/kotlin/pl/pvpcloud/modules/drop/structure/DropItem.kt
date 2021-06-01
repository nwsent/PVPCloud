package pl.pvpcloud.modules.drop.structure

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class DropItem(
        val id: Int,
        val name: String,
        val height: Int,
        val percentage: Int,
        val minAmount: Int,
        val maxAmount: Int,
        val isFortune: Boolean,
        val material: Material
) {

    fun toItemStack(amount: Int): ItemStack =
            ItemStack(this.material, amount)

}