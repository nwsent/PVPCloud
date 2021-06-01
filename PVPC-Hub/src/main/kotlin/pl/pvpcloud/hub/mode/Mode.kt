package pl.pvpcloud.hub.mode

import fr.minuskube.inv.content.SlotPos
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.common.helpers.ItemsHelper

data class Mode(
        val name: String,
        val item: ItemHelper,
        val slot: SlotPos,
        val servers: ArrayList<String>
) {

    var isAvailable: Boolean = false
    val reason: String = "Tryb jest wylaczony!"
    var online: Int = 0

    fun getItem(): ItemStack {
        val item = this.item.toItemStack()
        val lore = item.itemMeta.lore
        lore.replaceAll {
            it
                    .rep("%online%", this.online.toString())
                    .rep("%status%", if (isAvailable) "&eOnline".fixColors() else "&cOffline".fixColors())
        }
        ItemsHelper.setLoreItemStack(item, lore)
        return item
    }
    
}