package pl.pvpcloud.party.repository

import pl.pvpcloud.party.PartyModule
import pl.pvpcloud.party.basic.Party
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PartyRepository(private val partyModule: PartyModule) {

    val parties = ConcurrentHashMap<Int, Party>()

    fun getPartyByUUID(uniqueId: UUID) : Party? {
        return this.parties.values.singleOrNull { it.hasMember(uniqueId) }
    }

    fun getPartyById(id: Int) : Party? {
        return this.parties[id]
    }

}