package pl.pvpcloud.statistics.api.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketStatsUpdate(
        val uniqueId: UUID,
        val collectionName: String
) : NatsPacket()