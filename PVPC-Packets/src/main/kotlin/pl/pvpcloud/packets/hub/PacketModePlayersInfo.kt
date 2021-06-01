package pl.pvpcloud.packets.hub

import pl.pvpcloud.nats.api.NatsPacket

data class PacketModePlayersInfo(val modeName: String, val online: Int) : NatsPacket()