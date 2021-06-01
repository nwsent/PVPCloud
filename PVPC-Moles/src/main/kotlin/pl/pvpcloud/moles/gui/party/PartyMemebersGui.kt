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
import pl.pvpcloud.moles.party.event.PartyKickEvent

class PartyMemebersGui(private val plugin: MolesPlugin) : InventoryProvider {

    companion object {
        fun getInventory(plugin: MolesPlugin, index: Int? = null): SmartInventory = SmartInventory.builder()
                .id("partyMemebersGui")
                .provider(PartyMemebersGui(plugin))
                .size(5, 9)
                .title("&8* &eLista graczy &7(&c${index ?: ""}&7)".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        val party = this.plugin.partyManager.getParty(player.uniqueId) ?: return

        party.getPlayers().forEach { players ->
            contents.add(ClickableItem.of(
                    HeadBuilder(players.name,
                            " ${ if(party.isLeader(player.uniqueId)) " &8* &eLider&8: &f${Bukkit.getPlayer(party.leader).name}" else " &8* &eCzłonek&8: &f${players.name}"}",
                            arrayListOf(" ", " ${ if(party.isLeader(player.uniqueId)) " &8* &fKilknij aby wyrzucic gracza." else " &8* &fCzłonek party."}", " ")
                    ).build()) {
                getInventory(plugin).open(player)
                this.plugin.server.pluginManager.callEvent(PartyKickEvent(player, players.name))
            })
        }

        contents.set(3, 7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWroć &8*").build()) {
            PartyGui.getInventory(plugin).open(player)
        })

    }

    override fun update(player: Player, contents: InventoryContents) {
    }

}