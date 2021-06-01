package pl.pvpcloud.connect.api.structure

import java.util.*

interface PlayerService {

    fun createPlayer(uuid: UUID, nick: String, proxyId: String, serverId: String)
    fun updatePlayerServer(uuid: UUID, serverId: String)
    fun updatePlayerComputerId(uuid: UUID, ip: String, computerUUID: ByteArray)
    fun deletePlayer(uuid: UUID)

}