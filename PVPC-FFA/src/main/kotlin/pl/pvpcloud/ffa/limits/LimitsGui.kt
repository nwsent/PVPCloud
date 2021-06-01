package pl.pvpcloud.ffa.limits

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.helpers.ItemsHelper

class LimitsGui(private val limitsModule: LimitsModule) : InventoryProvider  {

    companion object {
        fun getInventory(limitsModule: LimitsModule): SmartInventory = SmartInventory.builder()
            .id("limits")
            .provider(LimitsGui(limitsModule))
            .size(3, 9)
            .title("&8* &eSchowek".fixColors())
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))
        var i = 1
        this.limitsModule.config.limits.forEach {
            val id = it.id
            val material = Material.getMaterial(id)
            val limit = it.limit
            val data = it.data
            val deposit = this.limitsModule.limitsManager.getDeposit(player.uniqueId)?.getDeposit(id,data)?.amount ?: 0
            val item = it.iconMenu
            val stack = item.toItemStack()
            val meta = stack.itemMeta
            if (meta.hasLore()) {
                meta.lore = meta.lore.map {
                    it.rep("%deposit%",deposit.toString()).rep("%limit%", limit.toString()).rep("%amount%", ItemsHelper.countItem(player.inventory, material, data).toString())
                }
            }
            stack.itemMeta = meta
            contents.set(1, i, ClickableItem.of(stack) { _ ->
                this.limitsModule.limitsManager.getItemFromDeposit(player, it.id, it.data, it.limit)
            })
            i += 3
        }
    }

    override fun update(player: Player, contents: InventoryContents) {}
}