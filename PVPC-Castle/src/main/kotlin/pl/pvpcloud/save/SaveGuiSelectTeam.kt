package pl.pvpcloud.save

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class SaveGuiSelectTeam(private val saveModule: SaveModule) : InventoryProvider {

    companion object {
        fun getInventory(saveModule: SaveModule): SmartInventory = SmartInventory.builder()
                .id("saveInvSelectTeam")
                .provider(SaveGuiSelectTeam(saveModule))
                .size(3, 9)
                .title("&cZapisz ekwipunek".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.set(1, 3, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 14, "&8* &cAtakujący &8*").build()) {
            SaveGuiSelectKit.getInventory(this.saveModule, KitType.ATTACK).open(player)
        })
        contents.set(1, 5, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 11, "&8* &9Broniący &8*").build()) {
            SaveGuiSelectKit.getInventory(this.saveModule, KitType.DEFEND).open(player)
        })
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}