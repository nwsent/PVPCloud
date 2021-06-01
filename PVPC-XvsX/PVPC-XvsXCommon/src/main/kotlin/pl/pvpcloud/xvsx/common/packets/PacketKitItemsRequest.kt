package pl.pvpcloud.xvsx.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketKitItemsRequest(
        val uniqueId: UUID,
        val kitName: String
) : NatsPacket()