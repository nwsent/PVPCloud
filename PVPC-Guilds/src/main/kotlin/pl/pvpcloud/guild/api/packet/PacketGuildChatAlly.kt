package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.guild.api.structure.Guild
import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketGuildChatAlly(
        val message: String,
        val guild: Guild,
        val uuid: UUID
) : NatsPacket(), Serializable