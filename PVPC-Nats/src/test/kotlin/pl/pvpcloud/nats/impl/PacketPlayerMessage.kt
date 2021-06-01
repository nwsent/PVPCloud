package pl.pvpcloud.nats.impl

import pl.pvpcloud.nats.api.NatsPacket
import java.util.*

data class PacketPlayerMessage(val uuid: UUID, val messages: Array<String>) : NatsPacket() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PacketPlayerMessage

        if (uuid != other.uuid) return false
        if (!messages.contentEquals(other.messages)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + messages.contentHashCode()
        return result
    }

}