package pl.pvpcloud.packets.chat

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketPlayerHelpOp(
    val name: String,
    val serverId: String,
    val message: String
) : NatsPacket(), Serializable