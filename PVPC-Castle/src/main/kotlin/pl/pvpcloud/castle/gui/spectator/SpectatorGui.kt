package pl.pvpcloud.castle.gui.spectator

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.entity.Player
import pl.pvpcloud.castle.match.MatchTeam
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.extensions.fixColors

class SpectatorGui(private val matchTeam: MatchTeam) : InventoryProvider {

    companion object {
        fun getInventory(matchTeam: MatchTeam): SmartInventory = SmartInventory.builder()
                .id("spectatorGui")
                .provider(SpectatorGui(matchTeam))
                .size(3, 9)
                .title("&8* &eMenu obserwacji".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        var r = 0
        var c = 0
        matchTeam.getAlivePlayers().forEach { alive ->
            contents.set(r, c, ClickableItem.of(HeadBuilder(alive.name, "&8* &e${alive.name} &8*", arrayListOf(" ", "&8* &7Teleportuj się do tego gracza")).build()) {
                player.teleport(alive.location.clone().add(0.0, 1.0, 0.0))
            })
            c++
            if (c == 9) {
                c = 0
                r++
            }
        }
    }

    override fun update(player: Player, contents: InventoryContents) {
        var r = 0
        var c = 0
        matchTeam.getAlivePlayers().forEach { alive ->
            contents.set(r, c, ClickableItem.of(HeadBuilder(alive.name, "&8* &e${alive.name} &8*", arrayListOf(" ", "&8* &7Teleportuj się do tego gracza")).build()) {
                player.teleport(alive.location.clone().add(0.0, 1.0, 0.0))
            })
            c++
            if (c == 9) {
                c = 0
                r++
            }
        }
    }

}
