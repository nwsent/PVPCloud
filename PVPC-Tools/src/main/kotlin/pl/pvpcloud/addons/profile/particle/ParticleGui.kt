package pl.pvpcloud.addons.profile.particle

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
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

class ParticleGui(private val addonsModule: AddonsModule) : InventoryProvider {

    companion object {
        fun getInventory(addonsModule: AddonsModule): SmartInventory = SmartInventory.builder()
                .id("particleGui")
                .provider(ParticleGui(addonsModule))
                .size(6, 9)
                .title("&8* &eParticlesy".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        val user = ToolsAPI.findUserByUUID(player)

        contents.fillBorder()
        ParticleType.values().forEach {
            contents.set(it.slotPos,
                    ClickableItem.of(StatusItem(it.itemHelper,it.name == user.settings.particleType).build()) { _ ->
                        if (!player.hasPermission("addons.particle")) {
                            player.closeInventory()
                            player.sendTitle("", "&cEfekty możesz kupić w sklepie", 10, 50, 10)
                            return@of
                        }
                        user.settings.particleType = it.name
                        player.sendFixedMessage("&7» &fWybrales particles&8: &e${it.name}")
                        getInventory(addonsModule).open(player)
                    })
        }
        contents.set(4, 4, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWylacz particle &8*").build()) {
            user.settings.particleType = "-"
            getInventory(this.addonsModule).open(player)
        })
        contents.set(4, 7, ClickableItem.of(ItemBuilder(Material.BARRIER, "&8* &cWróc &8*").build()) {
            ProfileGui.getInventory(this.addonsModule).open(player)
        })
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}