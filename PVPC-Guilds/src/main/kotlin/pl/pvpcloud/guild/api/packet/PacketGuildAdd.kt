package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketGuildAdd(
        val guildId: UUID,
        val uuid: UUID
) : NatsPacket(), Serializable