package pl.pvpcloud.moles.common.packets

import pl.pvpcloud.nats.api.NatsPacket

data class PacketMatchResponse(
        val available: Boolean
) : NatsPacket()