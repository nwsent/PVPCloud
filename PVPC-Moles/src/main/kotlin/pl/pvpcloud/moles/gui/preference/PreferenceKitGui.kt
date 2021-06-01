package pl.pvpcloud.moles.gui.preference

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.save.KitAPI

class PreferenceKitGui(private val match: Match) : InventoryProvider {

    //todo jak bedzie mi sie nudzic to licznik aktualnie glosow

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
        this.match.votes.keys.forEach {
            contents.set(0, c, ClickableItem.of(KitAPI.getKit(it).inventory[0]) { _ ->
                this.match.handleVote(it)
                player.closeInventory()
            })
            c++
        }
    }

    override fun update(player: Player, contents: InventoryContents) {}
}