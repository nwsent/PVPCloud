package pl.pvpcloud.event.host

import org.bukkit.inventory.ItemStack

data class Kit(
        val icon: ItemStack,
        val armor: Array<ItemStack>,
        val inventory: Array<ItemStack>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kit

        if (!armor.contentEquals(other.armor)) return false
        if (!inventory.contentEquals(other.inventory)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = armor.contentHashCode()
        result = 31 * result + inventory.contentHashCode()
        return result
    }
}