package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.guild.api.structure.Guild
import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

class PacketGuildTimeOut(
        val guild: Guild
) : NatsPacket(), Serializable