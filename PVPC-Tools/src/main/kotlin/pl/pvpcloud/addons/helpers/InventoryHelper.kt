package pl.pvpcloud.addons.helpers

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.helpers.ItemEnchantedHelper
import pl.pvpcloud.common.helpers.ItemHelper

object InventoryHelper {

    fun InventoryContents.fillBorder() : InventoryContents =
            this.fillBorders(
                    ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, " ").build()))


    fun InventoryContents.fillBorder(data: Short) : InventoryContents =
            this.fillBorders(
                    ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, data, " ").build()))
}