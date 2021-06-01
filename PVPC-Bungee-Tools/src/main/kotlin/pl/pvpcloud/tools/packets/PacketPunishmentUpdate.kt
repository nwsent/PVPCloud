package pl.pvpcloud.tools.packets

import pl.pvpcloud.nats.api.NatsPacket
import pl.pvpcloud.tools.basic.Ban
import pl.pvpcloud.tools.basic.Blacklist
import pl.pvpcloud.tools.basic.Mute

data class PacketBanAdd(
        val ban: Ban
) : NatsPacket()

data class PacketBlacklistAdd(
        val blacklist: Blacklist
) : NatsPacket()

data class PacketMuteAdd(
        val mute: Mute
) : NatsPacket()

data class PacketBanRemove(
        val ban: Ban
) : NatsPacket()

data class PacketBlacklistRemove(
        val blacklist: Blacklist
) : NatsPacket()

data class PacketMuteRemove(
        val mute: Mute
) : NatsPacket()
