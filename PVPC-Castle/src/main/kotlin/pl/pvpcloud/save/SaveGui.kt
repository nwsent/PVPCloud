package pl.pvpcloud.save

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.InventoryListener
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class SaveGui(private val saveModule: SaveModule, private val saveSectionHelper: SaveSectionHelper, private val kitType: KitType) : InventoryProvider {

    companion object {
        fun getInventory(saveModule: SaveModule, saveSectionHelper: SaveSectionHelper, kitType: KitType): SmartInventory = SmartInventory.builder()
                .id("save")
                .provider(SaveGui(saveModule, saveSectionHelper, kitType))
                .size(3, 9)
                .title("&8* &eZapisz ekwipunek".fixColors())
                .listener(InventoryListener(InventoryCloseEvent::class.java) {
                    Bukkit.getPluginManager().callEvent(PlayerSaveCloseEvent(it.player as Player))
                })
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.set(1, 1, ClickableItem.of(ItemBuilder(Material.LAVA_BUCKET, "&4Resetuj ustawienie", arrayListOf("", "&7Zresetuje do domyślnego zestawu")).build()) { _ ->
            this.saveModule.saveManager.giveItemsToSaveReset(player, this.saveSectionHelper.kitName, this.kitType)
        })
        contents.set(1, 4, ClickableItem.of(ItemBuilder(Material.NAME_TAG, "&eZapisz", arrayListOf("", "&7Zapisuje ulożenie itemów w ekwipunku")).build()) { _ ->
            this.saveModule.saveManager.createSave(player, this.saveSectionHelper.kitName, this.kitType)
            player.closeInventory()
        })
        contents.set(1, 7, ClickableItem.of(ItemBuilder(Material.BARRIER, "&cWróć bez zapisu", arrayListOf("", "&7Zostawia aktualne ułożenie")).build()) { _ ->
            SaveGuiSelectKit.getInventory(this.saveModule, this.kitType).open(player)
        })
    }

    override fun update(player: Player, contents: InventoryContents) {}
}