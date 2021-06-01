package pl.pvpcloud.grouptp.common.packets

import pl.pvpcloud.nats.api.NatsPacket

data class PacketStatusRequest(
        val time: Long = System.currentTimeMillis()
) : NatsPacket()