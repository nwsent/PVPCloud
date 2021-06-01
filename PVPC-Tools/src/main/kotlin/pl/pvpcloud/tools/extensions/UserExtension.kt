package pl.pvpcloud.tools.extensions

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.basic.User
import pl.pvpcloud.tools.packets.PacketPlayerUpdate

fun User.sendUpdate() = NetworkAPI.publish { PacketPlayerUpdate(this) }