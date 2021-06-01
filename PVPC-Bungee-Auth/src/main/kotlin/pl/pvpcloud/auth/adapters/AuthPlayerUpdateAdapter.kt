package pl.pvpcloud.auth.adapters

import pl.pvpcloud.auth.AuthPlugin
import pl.pvpcloud.auth.basic.AuthPlayer
import pl.pvpcloud.auth.packets.PacketAuthPlayerUpdate
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter

class AuthPlayerUpdateAdapter(private val plugin: AuthPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (id != NetworkAPI.id && packet is PacketAuthPlayerUpdate) {
            var authPlayer = this.plugin.authRepository.getAuthPlayer(packet.name)
            if (authPlayer == null) {
                authPlayer = AuthPlayer(packet.name)
                this.plugin.authRepository.addPlayer(authPlayer)
            }
            authPlayer.isLogged = false
            authPlayer.password = packet.password
        }
    }
}