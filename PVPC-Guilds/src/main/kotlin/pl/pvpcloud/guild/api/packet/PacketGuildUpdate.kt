package pl.pvpcloud.guild.api.packet

import pl.pvpcloud.guild.api.structure.Guild
import pl.pvpcloud.guild.impl.structure.GuildImpl
import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

class PacketGuildUpdate(
        val guild: GuildImpl
) : NatsPacket(), Serializable {

    constructor(guild: Guild) : this(guild as GuildImpl)

}