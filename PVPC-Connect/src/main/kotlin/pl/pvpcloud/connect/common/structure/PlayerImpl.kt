package pl.pvpcloud.connect.common.structure

import pl.pvpcloud.connect.api.structure.Player
import pl.pvpcloud.connect.common.packet.*
import pl.pvpcloud.nats.NetworkAPI
import java.util.*

class PlayerImpl(
        override val uuid: UUID,
        override val nick: String,
        override val proxyId: String,
        override var serverId: String
) : Player {

    override var ip: String? = null
    override var computerId: ByteArray? = null

    override fun sendMessage(message: String) {
        NetworkAPI.publish(this.serverId) {
            PacketPlayerMessage(this.uuid, message)
        }
    }

    override fun sendActionbar(message: String) {
        NetworkAPI.publish(this.serverId) {
            PacketPlayerActionbar(this.uuid, message)
        }
    }

    override fun sendTitle(title: String, subtitle: String) {
        NetworkAPI.publish(this.serverId) {
            PacketPlayerTitle(this.uuid, title, subtitle)
        }
    }

    override fun connect(id: String) {
        NetworkAPI.publish(this.proxyId) {
            PacketPlayerConnect(this.uuid, id)
        }
    }

    override fun kick(message: String) {
        NetworkAPI.publish(this.serverId) {
            PacketPlayerKick(this.uuid, message)
        }
    }

}