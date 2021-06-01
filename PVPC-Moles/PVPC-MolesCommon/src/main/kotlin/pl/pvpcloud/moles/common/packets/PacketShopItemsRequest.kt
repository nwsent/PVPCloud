package pl.pvpcloud.moles.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketShopItemsRequest(
        val uniqueId: UUID
) : NatsPacket()