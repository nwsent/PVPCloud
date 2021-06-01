package pl.pvpcloud.moles.gui.party

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.party.event.PartyDeleteEvent
import pl.pvpcloud.moles.party.event.PartyLeaveEvent

class PartyGui(private val plugin: MolesPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: MolesPlugin): SmartInventory = SmartInventory.builder()
                .id("partyGui")
                .provider(PartyGui(plugin))
                .size(3, 9)
                .title("&8* &eTwoje party".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        val party = this.plugin.partyManager.getParty(player.uniqueId)
                ?: return
        contents.set(1, 1, ClickableItem.of(ItemBuilder(Material.BOOK, "&8* &fCzłonkowie &7(&c${party.getPlayers().size}&7) &8*").build()) {
            PartyMemebersGui.getInventory(plugin, party.getPlayers().size).open(player)
        })

        contents.set(1, 4, ClickableItem.empty(HeadBuilder(Bukkit.getPlayer(party.leader).name, "&8* &eLider&8: &f${Bukkit.getPlayer(party.leader).name}", arrayListOf(" ")).build()))

        if(party.isLeader(player.uniqueId)) {
            contents.set(1,7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &fUsun party &8*").build()) {
                this.plugin.server.pluginManager.callEvent(PartyDeleteEvent(player))
                player.closeInventory()
            })
        } else {
            contents.set(1,7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &fOpusc party &8*").build()) {
                this.plugin.server.pluginManager.callEvent(PartyLeaveEvent(player, true))
                player.closeInventory()
            })
        }

    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}