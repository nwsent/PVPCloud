package pl.pvpcloud.castle.gui.preferences

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class PreferencesGui(private val plugin: CastlePlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: CastlePlugin): SmartInventory = SmartInventory.builder()
                .id("preferencesGui")
                .provider(PreferencesGui(plugin))
                .size(3, 9)
                .title("&8* &ePreferencje".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        contents.set(1, 3, ClickableItem.of(ItemBuilder(Material.CHEST, "&8* &eWybierz mapę &8*", arrayListOf("", "&8* &7Wybierz swoją ulubioną mape! ", " ")).build()) {
            PreferencesMapsGui.getInventory(plugin).open(player)
        })

        contents.set(1, 5, ClickableItem.of(ItemBuilder(Material.CHEST, "&8* &eWybierz drużynę &8*", arrayListOf("", "&8* &7Wybierz drużyne ", " ")).build()) {
            PreferencesTeamGui.getInventory(plugin).open(player)
        })
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}