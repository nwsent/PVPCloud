package pl.pvpcloud.guild.api.packet

import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload
import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

class PacketGuildRecruitment(
        val packet: PacketPlayOutCustomPayload
) : NatsPacket(), Serializable