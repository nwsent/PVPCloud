package pl.pvpcloud.connect.common.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerUpdateComputerId(
        val uniqueId: UUID,
        val ip: String,
        val computerId: ByteArray
) : Serializable, NatsPacket() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PacketPlayerUpdateComputerId

        if (uniqueId != other.uniqueId) return false
        if (ip != other.ip) return false
        if (!computerId.contentEquals(other.computerId)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uniqueId.hashCode()
        result = 31 * result + ip.hashCode()
        result = 31 * result + computerId.contentHashCode()
        return result
    }

}