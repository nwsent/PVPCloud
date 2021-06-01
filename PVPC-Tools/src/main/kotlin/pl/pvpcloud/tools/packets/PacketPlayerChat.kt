package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketPlayerChat(
        val sender: String,
        val message: String
) : NatsPacket(), Serializable