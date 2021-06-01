package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketGlobalJoinMessage(
        val message: String
) : NatsPacket(), Serializable