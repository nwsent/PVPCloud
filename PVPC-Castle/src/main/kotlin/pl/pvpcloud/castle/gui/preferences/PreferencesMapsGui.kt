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
import pl.pvpcloud.common.extensions.sendFixedMessage

class PreferencesMapsGui(private val plugin: CastlePlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: CastlePlugin): SmartInventory = SmartInventory.builder()
                .id("preferencesArenaGui")
                .provider(PreferencesMapsGui(plugin))
                .size(3, 9)
                .title("&8* &ePreferencje &8&l- &fWybierz mape".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        this.plugin.arenaManager.arenasProfile.forEach {
            contents.add(ClickableItem.of(it.icon.build()) { _ ->
                val queueWait = this.plugin.queueManager.getQueueWait(player.uniqueId)
                        ?: return@of
                queueWait.handleVote(player.uniqueId, it.name)
                player.sendFixedMessage("&8* &fZagłosowałeś na arene&8: &e${it.name}")
                player.closeInventory()
            })
        }
    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}