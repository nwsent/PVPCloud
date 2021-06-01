package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import pl.pvpcloud.tools.basic.User
import java.io.Serializable

class PacketPlayerUpdate(
        val user: User
) : NatsPacket(), Serializable