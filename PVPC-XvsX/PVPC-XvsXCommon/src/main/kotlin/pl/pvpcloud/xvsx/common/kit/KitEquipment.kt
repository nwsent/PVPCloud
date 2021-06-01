package pl.pvpcloud.xvsx.common.kit

import org.bukkit.inventory.ItemStack

data class KitEquipment(
        val armor: Array<ItemStack>,
        val contents: Array<ItemStack>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KitEquipment

        if (!armor.contentEquals(other.armor)) return false
        if (!contents.contentEquals(other.contents)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = armor.contentHashCode()
        result = 31 * result + contents.contentHashCode()
        return result
    }
}