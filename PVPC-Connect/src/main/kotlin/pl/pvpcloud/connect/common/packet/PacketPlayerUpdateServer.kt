package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerUpdateServer(
        val uniqueId: UUID,
        val serverId: String
) : Serializable, NatsPacket()