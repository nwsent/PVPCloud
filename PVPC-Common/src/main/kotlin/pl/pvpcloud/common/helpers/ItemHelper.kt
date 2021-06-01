package pl.pvpcloud.common.helpers

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.bukkitName

open class ItemHelper(
        val name: String,
        private val id: Int,
        private val amount: Int = 1,
        private val data: Short = 0,
        private val lore: List<String> = arrayListOf()
) {
    companion object {
        val DEFAULT = ItemHelper("Brak Nazwy", Material.STONE.id)
    }

    constructor(item: ItemStack) : this(
            if (item.hasItemMeta() && item.itemMeta.hasDisplayName()) item.itemMeta.displayName else item.type.bukkitName(),
            item.type.id,
            item.amount,
            item.durability,
            if (item.hasItemMeta() && item.itemMeta.hasLore()) item.itemMeta.lore else arrayListOf()
    )

    fun toItemStack() = ItemBuilder(Material.getMaterial(id), amount, data, name, lore).build()
}