package pl.pvpcloud.grouptp.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketPrepareMatch(
        val players: Set<UUID>,
        val kitName: String
) : NatsPacket()