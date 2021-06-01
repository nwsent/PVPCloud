package pl.pvpcloud.packets.hub

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketTransfer(
    val uniqueId: UUID,
    val arenaName: String
) : NatsPacket(), Serializable {

    override fun toString(): String {
        return "PacketTransfer(uniqueId=$uniqueId, arenaName='$arenaName')"
    }
}

