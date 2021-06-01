package pl.pvpcloud.grouptp.hub.save

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.InventoryListener
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.kit.Kit

class SaveGui(private val plugin: GroupTpPlugin, private val kit: Kit) : InventoryProvider {

    companion object {
        fun getInventory(plugin: GroupTpPlugin, kit: Kit): SmartInventory = SmartInventory.builder()
            .id("save")
            .provider(SaveGui(plugin, kit))
            .size(3, 9)
            .title("&8* &eZapisz ekwipunek".fixColors())
            .listener(InventoryListener(InventoryCloseEvent::class.java) {
                Bukkit.getPluginManager().callEvent(PlayerSaveCloseEvent(it.player as Player))
            })
            .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        this.plugin.saveManager.giveItemsToSave(player, this.kit.name)
        contents.set(1, 1, ClickableItem.of(ItemBuilder(Material.LAVA_BUCKET, "&4Resetuj ustawienie", arrayListOf("", "&7Zresetuje do domyślnego zestawu")).build()) { _ ->
            this.plugin.saveManager.giveItemsToSaveReset(player, this.kit.name)
        })
        contents.set(1, 4, ClickableItem.of(ItemBuilder(Material.NAME_TAG, "&eZapisz", arrayListOf("", "&7Zapisuje ulożenie itemów w ekwipunku")).build()) { _ ->
            this.plugin.saveManager.createSave(player, this.kit.name)
            player.closeInventory()
        })
        contents.set(1, 7, ClickableItem.of(ItemBuilder(Material.BARRIER, "&cWróć bez zapisu", arrayListOf("", "&7Zostawia aktualne ułożenie")).build()) { _ ->
            player.closeInventory()
        })
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}