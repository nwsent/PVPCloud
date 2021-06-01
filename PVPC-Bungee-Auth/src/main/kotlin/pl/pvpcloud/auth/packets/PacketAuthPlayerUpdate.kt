package pl.pvpcloud.auth.packets

import pl.pvpcloud.auth.basic.AuthPlayer
import pl.pvpcloud.nats.api.NatsPacket

data class PacketAuthPlayerUpdate(
        val name: String,
        val password: String
) : NatsPacket() {

    constructor(authPlayer: AuthPlayer) : this(authPlayer.name, authPlayer.password)
}