package pl.pvpcloud.common.builders

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.fixColors

class ItemBuilder(
        val material: Material,
        val amount: Int = 1,
        val data: Short = 0,
        val name: String = "",
        val lore: List<String> = arrayListOf(),
        val map: Map<Enchantment, Int> = HashMap()
) {

    constructor(material: Material, name: String, amount: Int, data: Short) : this(material, amount, data, name)

    constructor(material: Material, name: String) : this(material, 1, 0, name)
    constructor(material: Material, name: String, lore: List<String>) : this(material, 1, 0, name, lore)
    constructor(material: Material, lore: List<String>) : this(material, 1, 0, "", lore)
    constructor(material: Material, amount: Int, name: String) : this(material, amount, 0, name)
    constructor(material: Material, amount: Int, lore: List<String>) : this(material, amount, 0, "", lore)
    constructor(material: Material, amount: Int, name: String, lore: List<String>) : this(material, amount, 0, name, lore)
    constructor(material: Material, name: String, enchant: Map<Enchantment, Int>) : this(material, 1, 0, name, arrayListOf(), enchant)
    constructor(material: Material, enchant: Map<Enchantment, Int>) : this(material, 1, 0, "", arrayListOf(), enchant)

    fun build(): ItemStack {
        var fixAmount = amount
        if (fixAmount > material.maxStackSize) {
            fixAmount = material.maxStackSize
        } else if (fixAmount <= 0) {
            fixAmount = 1
        }
        val item = ItemStack(material, fixAmount, data)
        map.forEach {
            if (it.value > it.key.maxLevel) item.addUnsafeEnchantment(it.key, it.value) else item.addEnchantment(it.key, it.value)
        }
        val meta = item.itemMeta ?: return item
        if (name.isNotEmpty()) {
            meta.displayName = name.fixColors()
        }
        val fixLore = arrayListOf<String>()
        this.lore.forEach {
            fixLore.add(it.fixColors())
        }
        meta.lore = fixLore
        item.itemMeta = meta
        return item
    }
}