package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket

data class PacketProxyUpdate(
        val whiteListStatus: Boolean,
        val whiteListReason: String,
        val whiteListPlayers: ArrayList<String>,
        val description: ArrayList<String>,
        val slotMaxShow: Int,
        val slotMaxOnline: Int
) : NatsPacket()