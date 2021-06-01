package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerCreate(val uuid: UUID, val nick: String, val proxyId: String, val serverId: String) : NatsPacket(), Serializable {

    override fun toString(): String {
        return "PacketPlayerCreate(uuid=$uuid, nick='$nick', proxyId='$proxyId', serverId='$serverId')"
    }

}