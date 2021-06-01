package pl.pvpcloud.connect.common.adapter

import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.common.packet.PacketPlayerUpdateComputerId
import pl.pvpcloud.connect.common.packet.PacketPlayerUpdateServer
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.redis.RedisAPI

class PlayerUpdateAdapter(private val playerRepository: PlayerRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        when (packet) {
            is PacketPlayerUpdateServer -> {
                val player = this.playerRepository.playerMap[packet.uniqueId]
                        ?: return
                player.serverId = packet.serverId

                RedisAPI.getCommands {
                    it.hset("pvpc-connect-players", player.uuid.toString(), player)
                }
            }
            is PacketPlayerUpdateComputerId -> {
                if (id == NetworkAPI.id) {
                    return
                }
                val player = this.playerRepository.playerMap[packet.uniqueId]
                        ?: return
                player.ip = packet.ip
                player.computerId = packet.computerId

                RedisAPI.getCommands {
                    it.hset("pvpc-connect-players", player.uuid.toString(), player)
                }
            }
        }
    }
}