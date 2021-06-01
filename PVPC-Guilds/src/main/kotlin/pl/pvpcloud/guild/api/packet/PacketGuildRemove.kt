package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketGuildRemove(
        val guildId: UUID,
        val uuid: UUID,
        val whoRemoved: UUID
) : NatsPacket(), Serializable