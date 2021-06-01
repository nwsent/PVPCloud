package pl.pvpcloud.save

import org.bukkit.inventory.ItemStack

data class Kit(
        val name: String,
        val version: Int,
        val kitType: KitType,
        val inventory: Array<ItemStack>,
        val armor: Array<ItemStack>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kit

        if (name != other.name) return false
        if (version != other.version) return false
        if (kitType != other.kitType) return false
        if (!inventory.contentEquals(other.inventory)) return false
        if (!armor.contentEquals(other.armor)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + version
        result = 31 * result + kitType.hashCode()
        result = 31 * result + inventory.contentHashCode()
        result = 31 * result + armor.contentHashCode()
        return result
    }
}