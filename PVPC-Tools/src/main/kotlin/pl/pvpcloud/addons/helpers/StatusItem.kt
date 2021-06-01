package pl.pvpcloud.addons.helpers

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.builders.ItemBuilder

class StatusItem(
        val itemStack: ItemBuilder,
        private val status: Boolean) {

    fun build() =
        ItemBuilder(itemStack.material, itemStack.amount, itemStack.data, itemStack.name, itemStack.lore, if(status) mapOf(Pair(Enchantment.DURABILITY, 5)) else mapOf()).build()

}