package pl.pvpcloud.xvsx.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

class PacketKitItemsResponse(
        val uniqueId: UUID,
        val armor: String,
        val inventory: String
) : NatsPacket()