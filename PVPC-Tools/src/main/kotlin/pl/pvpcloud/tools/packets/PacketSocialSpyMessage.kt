package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketSocialSpyMessage(
        val message: String
) : NatsPacket(), Serializable