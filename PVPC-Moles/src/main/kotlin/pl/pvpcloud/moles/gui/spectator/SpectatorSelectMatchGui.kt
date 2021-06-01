package pl.pvpcloud.moles.gui.spectator

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.MatchState

class SpectatorSelectMatchGui(private val plugin: MolesPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: MolesPlugin): SmartInventory = SmartInventory.builder()
                .id("spectatorSelectMatchGui")
                .provider(SpectatorSelectMatchGui(plugin))
                .size(3, 9)
                .title("&8* &eMenu obserwacji".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        var c = 1
        var r = 1
        for (match in this.plugin.matchManager.matches.values) {
            if (!match.isState(MatchState.FIGHTING)) {
                continue
            }
            if (c == 8) {
                r = 2
                c = 1
            }
            contents.set(r, c, ClickableItem.of(ItemBuilder(Material.STAINED_CLAY, 1, 5, "&8* &e${match.arena.worldName} &8*",
                    arrayListOf("",
                            "&8* &7Koniec za&8: &e${DataHelper.formatTime(match.timeToEnd - System.currentTimeMillis())}",
                            "",
                            "&8* &7Atakujących&8: &e${match.attackTeam.getAliveCount()}",
                            "&8* &7Broniących&8: &e${match.defendTeam.getAliveCount()}")).build()) {
                this.plugin.spectateManager.addSpectate(player, match, false)
                player.teleport(match.arena.getDefendSpawn())
            })
            c++
        }
    }

    override fun update(player: Player?, contents: InventoryContents?) {}
}