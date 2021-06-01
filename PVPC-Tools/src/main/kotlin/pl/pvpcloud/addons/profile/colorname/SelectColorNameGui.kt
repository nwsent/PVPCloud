package pl.pvpcloud.addons.profile.colorname

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

class SelectColorNameGui(private val addonsModule: AddonsModule) : InventoryProvider {

    companion object {
        fun getInventory(addonsModule: AddonsModule): SmartInventory = SmartInventory.builder()
                .id("nameColorGui")
                .provider(SelectColorNameGui(addonsModule))
                .size(5, 9)
                .title("&8* &eWybierz kolor nicku".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player)

        contents.fillBorder()
        ColorType.values().forEach {
            contents.set(it.slotPos,
                    ClickableItem.of(StatusItem(it.itemHelper, user.settings.chatNameColorType == it.name).build()) { _ ->
                        if (!player.hasPermission("addons.namecolor")) {
                            player.closeInventory()
                            player.sendTitle("", "&cKolor nicku mozesz zakupić w sklepie!", 0, 60, 0)
                            return@of
                        }
                        user.settings.chatNameColorType = it.name
                        player.sendFixedMessage("&7» &fWybrales kolor twojego nicku&8: ${ChatColor.valueOf(it.name)}${it.colorName}")
                        getInventory(this.addonsModule).open(player)
                    })


        }

        contents.set(3, 4,
                ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWylacz kolor nicku &8*").build()) {
                    user.settings.chatNameColorType = "GRAY"
                    getInventory(this.addonsModule).open(player)
                })

        contents.set(3, 7,
                ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
                    ProfileGui.getInventory(this.addonsModule).open(player)
                })
    }

    override fun update(player: Player, contents: InventoryContents) {}

}