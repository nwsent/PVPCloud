package pl.pvpcloud.save

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.castle.profile.ProfileState
import pl.pvpcloud.common.extensions.fixColors

class SaveGuiSelectKit(private val saveModule: SaveModule, private val kitType: KitType) : InventoryProvider {

    companion object {
        fun getInventory(saveModule: SaveModule, kitType: KitType): SmartInventory = SmartInventory.builder()
                .id("saveInvSelectKit")
                .provider(SaveGuiSelectKit(saveModule, kitType))
                .size(3, 9)
                .title("&cZapisz ekwipunek".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        this.saveModule.saveConfig.sections.forEach {
            contents.set(it.slot, ClickableItem.of(it.item.toItemStack()) { _ ->
                this.saveModule.saveManager.giveItemsToSave(player, it.kitName, this.kitType)
                //ShopAPI.giveItems(player)
                val profile = this.saveModule.plugin.profileManager.getProfile(player)
                profile.profileState = ProfileState.SAVEING
                SaveGui.getInventory(this.saveModule, it, this.kitType).open(player)
            })
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}