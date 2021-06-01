package pl.pvpcloud.ffa.shop

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*

class PurchasesPlayer(
        val uuid: UUID,
        val purchases: ArrayList<Purchase>
) : DatabaseEntity() {

    override val id: String
        get() = this.uuid.toString()

    override val key: String
        get() = "uuid"

    override val collection: String
        get() = "shop-purchases-ffa"

    val purchasesFilter get() = this.purchases.filter { !it.expired }
}