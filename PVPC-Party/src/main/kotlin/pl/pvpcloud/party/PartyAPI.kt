package pl.pvpcloud.party

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.party.basic.Party
import pl.pvpcloud.party.gui.PartyGui
import java.util.*

object PartyAPI {

    internal lateinit var partyModule: PartyModule

    fun getParty(uniqueId: UUID): Party? = this.partyModule.partyRepository.getPartyByUUID(uniqueId)
    fun getParty(id: Int): Party? = this.partyModule.partyRepository.getPartyById(id)

    fun openPartyGui(player: Player) {
        PartyGui.getInventory(this.partyModule).open(player)
    }
}