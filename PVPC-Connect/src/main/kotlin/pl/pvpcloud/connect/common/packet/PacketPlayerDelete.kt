package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerDelete(val uuid: UUID) : NatsPacket(), Serializable {

    override fun toString(): String {
        return "PacketPlayerDelete(uuid=$uuid)"
    }

}