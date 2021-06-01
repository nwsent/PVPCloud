package pl.pvpcloud.moles.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

class PacketShopItemsResponse(
        val uniqueId: UUID,
        val items: String
) : NatsPacket()