package pl.pvpcloud.grouptp.hub.shop

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
        get() = "shop-purchases-gtp"

    val purchasesActive get() = this.purchases.filter { !it.expired }
}