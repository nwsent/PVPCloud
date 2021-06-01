package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerTitle(val uuid: UUID, val title: String, val subtitle: String) : Serializable, NatsPacket() {

    override fun toString(): String {
        return "PacketPlayerTitle(uuid=$uuid, title='$title', subtitle='$subtitle')"
    }

}