package pl.pvpcloud.connect.common.adapter

import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.common.packet.PacketPlayerDelete
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.redis.RedisAPI

class PlayerDeleteAdapter(private val playerRepository: PlayerRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerDelete) {
            this.playerRepository.playerMap.remove(packet.uuid)

            RedisAPI.getCommands {
                it.hdel("pvpc-connect-players", packet.uuid.toString())
            }
        }
    }

}