package pl.pvpcloud.moles.gui.spectator

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.moles.match.MatchTeam

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
        for (alive in matchTeam.getAlivePlayers()) {
            contents.set(r, c, ClickableItem.of(getHead(alive)) {
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
        for (alive in matchTeam.getAlivePlayers()) {
            contents.set(r, c, ClickableItem.of(getHead(alive)) {
                player.teleport(alive.location.clone().add(0.0, 1.0, 0.0))
            })
            c++
            if (c == 9) {
                c = 0
                r++
            }
        }
    }

    private fun getHead(player: Player): ItemStack {
        val skull = ItemBuilder(Material.SKULL_ITEM, 1, 3.toShort(),
                "&e${player.name}",
                arrayListOf("", "&8* &7Teleportuj się do tego gracza")).build()
        val meta = skull.itemMeta as SkullMeta
        meta.owner = player.name
        skull.itemMeta = meta
        return skull
    }
}
