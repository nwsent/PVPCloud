package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket

data class PacketProxyCommand(val command: String) : NatsPacket()