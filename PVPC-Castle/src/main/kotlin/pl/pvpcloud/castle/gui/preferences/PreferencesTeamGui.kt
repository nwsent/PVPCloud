package pl.pvpcloud.castle.gui.preferences

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.queues.QueueType
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage

class PreferencesTeamGui(private val plugin: CastlePlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: CastlePlugin): SmartInventory = SmartInventory.builder()
                .id("preferencesTeamGui")
                .provider(PreferencesTeamGui(plugin))
                .size(3, 9)
                .title("&8* &ePreferencje &8&l- &fWybierz Team".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        val queueEntry = this.plugin.queueManager.queuesWait[this.plugin.queueManager.queuePlayer[player.uniqueId]]?.getQueueEntry(player.uniqueId)
                ?: return player.sendFixedMessage("&cTylko lider może tu coś zmienić!")

        contents.set(1, 3, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 14, "&8* &cAtakujący &8*", arrayListOf("", "&8* &7Ustawia preferencje na drużynę atakującą")).build()) {
            player.sendFixedMessage("&8* &fTeraz będziesz w drużynie&8: &cAtakującej")
            queueEntry.queueType = QueueType.ATTACK
            player.closeInventory()
        })
        contents.set(1, 5, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 11, "&8* &9Broniący &8*", arrayListOf("", "&8* &7Ustawia preferencje na drużynę broniącą")).build()) {
            player.sendFixedMessage("&8* &fTeraz będziesz w drużynie&8: &9Broniącej")
            queueEntry.queueType = QueueType.DEFEND
            player.closeInventory()
        })
    }

    override fun update(player: Player, contents: InventoryContents) {}

}
