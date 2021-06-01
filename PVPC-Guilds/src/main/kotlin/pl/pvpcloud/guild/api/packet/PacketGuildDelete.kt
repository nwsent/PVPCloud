package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketGuildDelete(
        val guildId: UUID,
        val tag: String,
        val name: String,
        val leaderUUID: UUID
) : NatsPacket(), Serializable