package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerConnect(val uuid: UUID, val id: String) : Serializable, NatsPacket() {

    override fun toString(): String {
        return "PacketPlayerConnect(uuid=$uuid, id='$id')"
    }

}