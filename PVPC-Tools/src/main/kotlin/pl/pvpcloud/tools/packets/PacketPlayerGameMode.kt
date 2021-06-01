package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

data class PacketPlayerGameMode(val uuid: UUID, val mode: Int) : NatsPacket(), Serializable