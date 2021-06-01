package pl.pvpcloud.moles.gui.preference

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep

class PreferenceKitGui(private val match: Match) : InventoryProvider {

    companion object {
        fun getInventory(match: Match): SmartInventory = SmartInventory.builder()
                .id("PreferenceKitGui")
                .provider(PreferenceKitGui(match))
                .size(1, 9)
                .title("&8* &eWybierz zestaw".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        var c = 0
        this.match.plugin.kitManager.kits.forEach { kit ->
            val item = kit.item.toItemStack()
            val meta = item.itemMeta
            val lore = mutableListOf(*meta.lore.toTypedArray())
            lore.replaceAll { it.rep("%votes%", this.match.getVotes(kit.name).toString())  }
            meta.lore = lore
            item.itemMeta = meta
            contents.set(0, c, ClickableItem.of(item) { _ ->
                this.match.handleVote(kit.name)
                player.closeInventory()
            })
            c++
        }
    }

    override fun update(player: Player, contents: InventoryContents) {
        var c = 0
        this.match.plugin.kitManager.kits.forEach { kit ->
            val item = kit.item.toItemStack()
            val meta = item.itemMeta
            val lore = mutableListOf(*meta.lore.toTypedArray())
            lore.replaceAll { it.rep("%votes%", this.match.getVotes(kit.name).toString())  }
            meta.lore = lore
            item.itemMeta = meta
            contents.set(0, c, ClickableItem.of(item) { _ ->
                this.match.handleVote(kit.name)
                player.closeInventory()
            })
            c++
        }
    }
}