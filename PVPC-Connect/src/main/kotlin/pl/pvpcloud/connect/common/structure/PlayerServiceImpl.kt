package pl.pvpcloud.connect.common.structure

import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.connect.common.packet.PacketPlayerCreate
import pl.pvpcloud.connect.common.packet.PacketPlayerDelete
import pl.pvpcloud.connect.common.packet.PacketPlayerUpdateComputerId
import pl.pvpcloud.connect.common.packet.PacketPlayerUpdateServer
import pl.pvpcloud.nats.NetworkAPI
import java.util.*

class PlayerServiceImpl : PlayerService {

    override fun createPlayer(uuid: UUID, nick: String, proxyId: String, serverId: String) {
        NetworkAPI.publish {
            PacketPlayerCreate(uuid, nick, proxyId, serverId)
        }
    }

    override fun updatePlayerServer(uuid: UUID, serverId: String) {
        NetworkAPI.publish {
            PacketPlayerUpdateServer(uuid, serverId)
        }
    }

    override fun updatePlayerComputerId(uuid: UUID, ip: String, computerUUID: ByteArray) {
        NetworkAPI.publish {
            PacketPlayerUpdateComputerId(uuid, ip, computerUUID)
        }
    }

    override fun deletePlayer(uuid: UUID) {
        NetworkAPI.publish {
            PacketPlayerDelete(uuid)
        }
    }



}