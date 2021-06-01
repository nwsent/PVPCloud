package pl.pvpcloud.connect.common.adapter

import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.common.packet.PacketPlayerCreate
import pl.pvpcloud.connect.common.structure.PlayerImpl
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.redis.RedisAPI

class PlayerCreateAdapter(private val playerRepository: PlayerRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerCreate) {
            this.playerRepository.playerMap.remove(packet.uuid)

            val player = PlayerImpl(packet.uuid, packet.nick, packet.proxyId, packet.serverId)
            this.playerRepository.playerMap[packet.uuid] = player

            RedisAPI.getCommands {
                it.hset("pvpc-connect-players", player.uuid.toString(), player)
            }
        }
    }

}