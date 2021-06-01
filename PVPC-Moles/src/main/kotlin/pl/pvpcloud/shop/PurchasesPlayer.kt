package pl.pvpcloud.shop

import pl.pvpcloud.database.api.DatabaseEntity
import java.util.*

class PurchasesPlayer(
        val uuid: UUID,
        val purchases: ArrayList<Purchase>
) : DatabaseEntity() {

    override val id: String
        get() = this.uuid.toString()

    override val collection: String
        get() = "shop-purchases-moles"

    override val key: String
        get() = "uuid"

    val purchasesFilter get() = this.purchases.filter { !it.expired }
}