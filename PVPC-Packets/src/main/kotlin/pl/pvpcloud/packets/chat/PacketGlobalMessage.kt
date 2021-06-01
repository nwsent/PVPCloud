package pl.pvpcloud.packets.chat

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketGlobalMessage(
        val message: String,
        val permission: String
) : NatsPacket(), Serializable