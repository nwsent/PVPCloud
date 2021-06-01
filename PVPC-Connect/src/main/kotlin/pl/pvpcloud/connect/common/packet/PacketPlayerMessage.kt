package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerMessage(val uuid: UUID, val message: String) : Serializable, NatsPacket() {

    override fun toString(): String {
        return "PacketPlayerMessage(uuid=$uuid, message='$message')"
    }

}