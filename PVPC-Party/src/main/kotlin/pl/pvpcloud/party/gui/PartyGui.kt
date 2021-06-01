package pl.pvpcloud.party.gui

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
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.event.PartyCreateEvent
import pl.pvpcloud.party.event.PartyDeleteEvent
import pl.pvpcloud.party.event.PartyLeaveEvent

class PartyGui(private val partyModule: PartyModule) : InventoryProvider {

    companion object {
        fun getInventory(partyModule: PartyModule): SmartInventory = SmartInventory.builder()
                .id("partyGui")
                .provider(PartyGui(partyModule))
                .size(5, 9)
                .title("&8* &eTwoje party".fixColors())
                .build()
    }

    override fun init(player: Player, contents: InventoryContents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 15, " ").build()))

        val party = this.partyModule.partyRepository.getPartyByUUID(player.uniqueId)
                ?: return player.sendFixedMessage(this.partyModule.config.partyYouDontHaveParty)

        contents.set(1, 1, ClickableItem.of(ItemBuilder(Material.BOOK, "&8* &fCzłonkowie &7(&c${party.members.size}&7) &8*").build()) {
            PartyMembersGui.getInventory(this.partyModule, party).open(player)
        })

        contents.set(1, 4, ClickableItem.empty(HeadBuilder(Bukkit.getPlayer(party.uniqueIdLeader).name, "&8* &eLider&8: &f${party.leaderMember.name}", arrayListOf(" ")).build()))

        if(party.uniqueIdLeader == player.uniqueId) {
            contents.set(1,7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &fUsun party &8*").build()) {
                this.partyModule.partyFactory.deleteParty(player)
                player.closeInventory()
            })
        } else {
            contents.set(1,7, ClickableItem.of(ItemBuilder(Material.REDSTONE, "&8* &fOpusc party &8*").build()) {
                this.partyModule.partyFactory.leaveParty(player,false)
                player.closeInventory()
            })
        }

    }

    override fun update(player: Player, contents: InventoryContents) {}

}