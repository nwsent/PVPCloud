package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketGlobalCommand(
        val command: String
) : NatsPacket(), Serializable