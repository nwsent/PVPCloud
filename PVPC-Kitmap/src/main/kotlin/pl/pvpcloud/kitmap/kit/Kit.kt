package pl.pvpcloud.kitmap.kit

data class Kit(
        val name: String,
        val nameVillager: String,
        val inventory: String,
        val armor: String
) {

    override fun equals(other : Any?) : Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Kit

        if (name != other.name) return false
        if (nameVillager != other.nameVillager) return false
        if (!inventory.contentEquals(other.inventory)) return false
        if (!armor.contentEquals(other.armor)) return false

        return true
    }

    override fun hashCode() : Int {
        var result = name.hashCode()
        result = 31 * result + nameVillager.hashCode()
        result = 31 * result + inventory.contentHashCode()
        result = 31 * result + armor.contentHashCode()
        return result
    }
}