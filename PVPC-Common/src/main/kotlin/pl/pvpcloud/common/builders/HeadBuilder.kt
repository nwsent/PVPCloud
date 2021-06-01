package pl.pvpcloud.common.builders

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class HeadBuilder(
        val nick: String = "",
        val name: String = "",
        val lore: List<String> = arrayListOf()
) {

    constructor(nick: String) : this(nick, "", arrayListOf())
    constructor(nick: String, name: String) : this(nick, name, arrayListOf())

    fun build(): ItemStack {
        val skull = ItemBuilder(Material.SKULL_ITEM, 1, 3.toShort(),
                name,
                lore).build()
        val meta = skull.itemMeta as SkullMeta
        meta.owner = nick
        skull.itemMeta = meta
        return skull
    }

}