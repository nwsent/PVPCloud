package pl.pvpcloud.addons.profile.colortag

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.helpers.InventoryHelper
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.addons.helpers.StatusItem
import pl.pvpcloud.addons.profile.ProfileGui
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.tools.ToolsAPI

class SelectColorTagGui(private val addonsModule: AddonsModule) : InventoryProvider {

    companion object {
        fun getInventory(addonsModule: AddonsModule): SmartInventory = SmartInventory.builder()
                .id("tagColorGui")
                .provider(SelectColorTagGui(addonsModule))
                .size(5, 9)
                .title("&8* &eWybierz kolor tagu".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player)

        contents.fillBorder()
        ColorTagType.values().forEach {
            contents.set(it.slotPos,
                    ClickableItem.of(StatusItem(it.itemHelper, user.settings.tagColorType == it.name).build()) { _ ->
                        if (!player.hasPermission("addons.tagcolor")) {
                            player.closeInventory()
                            player.sendTitle("", "&cKolor tagu mozesz zakupić w sklepie!", 0, 60, 0)
                            return@of
                        }
                        user.settings.tagColorType = it.name
                        player.sendFixedMessage("&7» &fWybrales kolor twojego tagu&8: ${ChatColor.valueOf(it.name)}${it.colorName}")
                        getInventory(addonsModule).open(player)
                    })
        }
        contents.set(3, 4,
                ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWylacz kolor tagu &8*").build()) {
                    user.settings.tagColorType = "WHITE"
                    getInventory(this.addonsModule).open(player)
                })

        contents.set(3, 7,
                ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
                    ProfileGui.getInventory(this.addonsModule).open(player)
                })
    }

    override fun update(player: Player, contents: InventoryContents) {}

}