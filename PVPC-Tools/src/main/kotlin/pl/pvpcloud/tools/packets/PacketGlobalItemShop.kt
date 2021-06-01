package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketGlobalItemShop(
        val text: ArrayList<String>,
        val title: String
) : NatsPacket(), Serializable