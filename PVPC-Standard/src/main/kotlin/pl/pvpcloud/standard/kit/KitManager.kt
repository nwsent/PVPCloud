package pl.pvpcloud.standard.kit

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import pl.pvpcloud.addons.AddonsAPI
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.tools.ToolsAPI

class KitManager(private val plugin: StandardPlugin) {

    fun giveKit(player: Player, kit: KitType, boolean: Boolean) {

        val playerInventory = player.inventory as PlayerInventory

        playerInventory.clear()
        playerInventory.armorContents = arrayOfNulls<ItemStack>(4)

        this.plugin.server.scheduler.runTaskLater(this.plugin, {
            playerInventory.contents = kit.inventory
            playerInventory.armorContents = kit.armor
            if (boolean) {
                playerInventory.setItem(4, kit.item)
                playerInventory.setItem(7, AddonsAPI.HEAD(player.name))
                playerInventory.setItem(8, AddonsAPI.HUB)
            }
        }, 2)

    }

}