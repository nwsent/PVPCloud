package pl.pvpcloud.common.helpers

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.bukkitName
import java.io.Serializable
open class ItemEnchantedHelper(
        val name: String,
        val id: Int,
        private val amount: Int = 1,
        private val data: Short = 0,
        private val lore: List<String> = arrayListOf(),
        private val enchants: HashMap<Int, Int> = HashMap()
) : Serializable {

    companion object {
        fun buildEnchantedItemOrNull(item: ItemStack) = if (item.type == Material.AIR) null else ItemEnchantedHelper(item)
    }

    constructor(item: ItemStack) : this(
            if (item.hasItemMeta() && item.itemMeta.hasDisplayName()) item.itemMeta.displayName else item.type.bukkitName(),
            item.type.id,
            item.amount,
            item.durability,
            if (item.hasItemMeta() && item.itemMeta.hasLore()) item.itemMeta.lore else arrayListOf(),
            item.enchantments.map { Pair(it.key.id, it.value) }.toMap(HashMap())
    )

    fun toItemStack() = ItemBuilder(Material.getMaterial(id), amount, data, name, lore, enchants.mapKeys { Enchantment.getById(it.key) }).build()
}