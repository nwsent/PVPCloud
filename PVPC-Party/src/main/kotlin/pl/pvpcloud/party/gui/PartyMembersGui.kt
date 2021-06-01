package pl.pvpcloud.party.gui

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.SmartInventory
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.InventoryProvider
import org.bukkit.Material
import org.bukkit.entity.Player
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.basic.Party
import pl.pvpcloud.party.event.PartyKickEvent

class PartyMembersGui(private val partyModule: PartyModule, private val party: Party) : InventoryProvider {

    companion object {
        fun getInventory(partyModule: PartyModule, party: Party): SmartInventory = SmartInventory.builder()
                .id("partyMemebersGui")
                .provider(PartyMembersGui(partyModule, party))
                .size(5, 9)
                .title("&8* &eLista graczy &7(&c${party.members.size}&7)".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        if (party.uniqueIdLeader == player.uniqueId) {

            this.party.members.forEach {
                val isLeader = party.uniqueIdLeader == it.uniqueId
                if (isLeader) {
                    contents.set(1, 1, ClickableItem.empty(HeadBuilder(it.name," &8* &eLider&8: &f${it.name}", arrayListOf("", " &8* &fKilknij aby wyrzucic gracza.")).build()))
                } else {
                    contents.add(ClickableItem.of(HeadBuilder(it.name, " &8* &eCzłonek&8: &f${it.name}", arrayListOf("", " &8* &fKilknij aby wyrzucic gracza.")).build()) { _ ->
                        getInventory(this.partyModule, this.party).open(player)
                        this.partyModule.partyFactory.kickPlayerFromParty(player, it.name)
                    })
                }
            }

        } else {
            this.party.members.forEach {
                val isLeader = party.uniqueIdLeader == it.uniqueId
                if (isLeader) {
                    contents.set(1, 1, ClickableItem.empty(HeadBuilder(it.name," &8* &eLider&8: &f${it.name}", arrayListOf("", " &8* &Lider party.")).build()))
                } else {
                    contents.add(ClickableItem.empty(HeadBuilder(it.name, " &8* &eCzłonek&8: &f${it.name}", arrayListOf("", " &8* &fCzłonek party.")).build()))
                }
            }
        }

        contents.set(3, 7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &cWroć &8*").build()) {
            PartyGui.getInventory(this.partyModule).open(player)
        })

    }

    override fun update(player: Player, contents: InventoryContents) {}

}