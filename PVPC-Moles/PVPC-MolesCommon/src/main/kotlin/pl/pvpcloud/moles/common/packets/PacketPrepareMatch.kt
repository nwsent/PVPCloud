package pl.pvpcloud.moles.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketPrepareMatch(
        val teamA: Set<UUID>,
        val teamB: Set<UUID>
) : NatsPacket()