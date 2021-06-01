package pl.pvpcloud.addons.profile.discoarmor

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.LeatherArmorMeta
import pl.pvpcloud.addons.AddonsModule
import pl.pvpcloud.addons.helpers.InventoryHelper
import pl.pvpcloud.addons.helpers.InventoryHelper.fillBorder
import pl.pvpcloud.addons.helpers.StatusItem
import pl.pvpcloud.addons.profile.ProfileGui
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.RandomHelper
import pl.pvpcloud.tools.ToolsAPI

class DiscoGui(private val addonsModule: AddonsModule): InventoryProvider {

    companion object {
        fun getInventory(addonsModule: AddonsModule): SmartInventory = SmartInventory.builder()
            .id("discoGui")
            .provider(DiscoGui(addonsModule))
            .size(6, 9)
            .title("&8* &eDisco zbroja".fixColors())
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player.uniqueId)

        contents.fillBorder()
        DiscoType.values().forEach { discoType ->
            contents.set(discoType.slotPos, ClickableItem.of(StatusItem(discoType.itemBuilder, user.settings.discoType == discoType.name).build()) { _ ->
                 if (!player.hasPermission("addons.disco")) {
                     player.closeInventory()
                     player.sendTitle("", "&cDisco zbroje możesz kupić w sklepie", 10, 50, 10)
                     return@of
                 }
                player.sendFixedMessage("&7» &fWybrales tryb disco&8: &e${discoType.name}")
                user.settings.discoType = discoType.name
                this.addonsModule.discoManager.init(player.uniqueId, discoType)
                getInventory(this.addonsModule).open(player)
            })
        }
        contents.set(4, 4,
                ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWylacz disco &8*").build()) {
                    user.settings.discoType = "-"
                    player.inventory.armorContents[4]
                    getInventory(this.addonsModule).open(player)
                })

        contents.set(4, 7,
                ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
                    ProfileGui.getInventory(this.addonsModule).open(player)
                })
    }

    override fun update(player: Player, contents: InventoryContents) {
        DiscoType.values().forEach {
            val discoItem = contents.get(it.slotPos).get()
            val meta = discoItem.item.itemMeta as LeatherArmorMeta
            meta.color = Color.fromRGB(RandomHelper.getRandomInt(0, 255), RandomHelper.getRandomInt(0, 255), RandomHelper.getRandomInt(0, 255))
            discoItem.item.itemMeta = meta
            contents.set(it.slotPos, discoItem)
        }
    }

}
