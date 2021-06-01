package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketGlobalTitle(
        val title: String,
        val subtitle: String
) : NatsPacket(), Serializable