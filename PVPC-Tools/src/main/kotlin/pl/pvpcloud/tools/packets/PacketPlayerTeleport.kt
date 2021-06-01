package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import java.io.Serializable
import java.util.*

class PacketPlayerTeleport(val fromUUID: UUID, val toUUID: UUID) : Serializable, NatsPacket()