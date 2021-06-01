package pl.pvpcloud.connect.bukkit

import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.connect.api.structure.Player
import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.connect.bukkit.adapters.PlayerActionbarAdapter
import pl.pvpcloud.connect.bukkit.adapters.PlayerKickAdapter
import pl.pvpcloud.connect.bukkit.adapters.PlayerMessageAdapter
import pl.pvpcloud.connect.bukkit.adapters.PlayerTitleAdapter
import pl.pvpcloud.connect.common.adapter.PlayerCreateAdapter
import pl.pvpcloud.connect.common.adapter.PlayerDeleteAdapter
import pl.pvpcloud.connect.common.adapter.PlayerUpdateAdapter
import pl.pvpcloud.connect.common.structure.PlayerRepositoryImpl
import pl.pvpcloud.connect.common.structure.PlayerServiceImpl
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.redis.RedisAPI

class ConnectPlugin : JavaPlugin() {

    lateinit var playerRepository: PlayerRepository
    lateinit var playerService: PlayerService

    override fun onEnable() {
        this.playerRepository = PlayerRepositoryImpl()
        this.playerService = PlayerServiceImpl()

        NetworkAPI.registerAdapters(
                PlayerCreateAdapter(this.playerRepository),
                PlayerDeleteAdapter(this.playerRepository),
                PlayerUpdateAdapter(this.playerRepository),
                PlayerMessageAdapter(),
                PlayerActionbarAdapter(),
                PlayerTitleAdapter(),
                PlayerKickAdapter()
        )

        RedisAPI.getCommands {
            it.hgetall("pvpc-connect-players").forEach { (_, player) ->
                if (player is Player) {
                    if (player.serverId != NetworkAPI.id) {
                        this.playerRepository.playerMap[player.uuid] = player
                    } else {
                        it.hdel("pvpc-connect-players", player.uuid.toString())
                        this.playerService.deletePlayer(player.uuid)
                    }
                }
            }
        }
        ConnectAPI.playerRepository = this.playerRepository
    }


    override fun onDisable() {
        RedisAPI.getCommands {
            it.hgetall("pvpc-connect-players").forEach { (uuid, player) ->
                if (player is Player && player.serverId == NetworkAPI.id) {
                    it.hdel("pvpc-connect-players", uuid)
                    this.playerService.deletePlayer(player.uuid)
                }
            }
        }
    }

}