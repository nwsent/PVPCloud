package pl.pvpcloud.standard.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.builders.ItemBuilder


enum class KitType(val kitName: String, val inventory: Array<ItemStack>, val armor: Array<ItemStack>, val item: ItemStack) {

    DIAX("&bDIAX",
            arrayOf(
                    ItemBuilder(Material.DIAMOND_SWORD, mapOf(Pair(Enchantment.DAMAGE_ALL, 3))).build()
            ),
            arrayOf(
                    ItemBuilder(Material.DIAMOND_BOOTS, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.DIAMOND_LEGGINGS, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.DIAMOND_CHESTPLATE, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.DIAMOND_HELMET, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build()
                    ),
            ItemBuilder(Material.IRON_CHESTPLATE, "&8* &fZmień tryb na: &7IRON &8*", mapOf(Pair(Enchantment.DURABILITY, 1))).build()
    ),

    IRON("&7IRON",
            arrayOf(
                    ItemBuilder(Material.IRON_SWORD, mapOf(Pair(Enchantment.DAMAGE_ALL, 3))).build()
            ),
            arrayOf(
                    ItemBuilder(Material.IRON_BOOTS, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.IRON_LEGGINGS, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.IRON_CHESTPLATE, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build(),
                    ItemBuilder(Material.IRON_HELMET, mapOf(Pair(Enchantment.PROTECTION_ENVIRONMENTAL, 3))).build()
            ),
            ItemBuilder(Material.DIAMOND_CHESTPLATE, "&8* &fZmień tryb na: &bDIAX &8*", mapOf(Pair(Enchantment.DURABILITY, 1))).build()
    )


}