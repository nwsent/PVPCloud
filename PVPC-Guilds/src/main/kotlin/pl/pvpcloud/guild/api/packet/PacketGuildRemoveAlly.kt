package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketGuildRemoveAlly(
        val sender: UUID,
        val target: UUID
) : NatsPacket(), Serializable