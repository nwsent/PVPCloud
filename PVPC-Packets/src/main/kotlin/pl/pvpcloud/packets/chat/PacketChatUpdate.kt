package pl.pvpcloud.packets.chat

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable

data class PacketChatSwitchLock(
    val lock: Boolean,
    val adminName: String
) : NatsPacket(), Serializable

data class PacketChatClear(
        val adminName: String
) : NatsPacket(), Serializable

data class PacketChatDelay(
        val delayTime: Long
) : NatsPacket(), Serializable