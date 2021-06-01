package pl.pvpcloud.xvsx.common.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketPrepareMatch(
        val kitName: String,
        val round: Int,
        val playersSize: Int,
        val teamA: Set<UUID>,
        val teamB: Set<UUID>
) : NatsPacket()