package pl.pvpcloud.castle.gui.spectator

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.castle.match.Match
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors

class SpectatorSelectTeamGui(private val match: Match) : InventoryProvider {

    companion object {
        fun getInventory(match: Match): SmartInventory = SmartInventory.builder()
                .id("spectatorSelectTeamGui")
                .provider(SpectatorSelectTeamGui(match))
                .size(3, 9)
                .title("&8* &eMenu obserwacji".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.set(1, 3, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 14, "&8* &cAtakujący &8*").build()) {
            SpectatorGui.getInventory(match.attackTeam).open(player)
        })
        contents.set(1, 5, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 11, "&8* &9Broniący &8*").build()) {
            SpectatorGui.getInventory(match.defendTeam).open(player)
        })
        contents.set(2, 4, ClickableItem.of(ItemBuilder(Material.BARRIER, 1, "&8* &cWyjdź z trybu obserwatora &8*").build()) {
            this.match.plugin.spectateManager.removeSpectate(player)
        })
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}