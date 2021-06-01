package pl.pvpcloud.connect.bungee

import com.google.common.util.concurrent.ThreadFactoryBuilder
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.connect.api.structure.Player
import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.connect.bungee.adapter.PlayerConnectAdapter
import pl.pvpcloud.connect.bungee.adapter.PlayerDisconnectAdapter
import pl.pvpcloud.connect.bungee.command.GlobalClearCommand
import pl.pvpcloud.connect.bungee.command.GlobalInfoCommand
import pl.pvpcloud.connect.bungee.command.GlobalKickCommand
import pl.pvpcloud.connect.bungee.command.GlobalUnbugCommand
import pl.pvpcloud.connect.bungee.listener.*
import pl.pvpcloud.connect.bungee.task.PlayerConnectTask
import pl.pvpcloud.connect.common.adapter.PlayerCreateAdapter
import pl.pvpcloud.connect.common.adapter.PlayerDeleteAdapter
import pl.pvpcloud.connect.common.adapter.PlayerUpdateAdapter
import pl.pvpcloud.connect.common.structure.PlayerRepositoryImpl
import pl.pvpcloud.connect.common.structure.PlayerServiceImpl
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.redis.RedisAPI
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ConnectPlugin : Plugin() {

    lateinit var playerRepository: PlayerRepository
    lateinit var playerService: PlayerService

    override fun onEnable() {
        this.playerRepository = PlayerRepositoryImpl()
        this.playerService = PlayerServiceImpl()

        NetworkAPI.registerAdapters(
                PlayerCreateAdapter(this.playerRepository),
                PlayerDeleteAdapter(this.playerRepository),
                PlayerConnectAdapter(),
                PlayerUpdateAdapter(this.playerRepository),
                PlayerDisconnectAdapter()
        )

        val pluginManager = ProxyServer.getInstance().pluginManager
        pluginManager.registerListener(this, PlayerConnectListener(this.playerService))
        pluginManager.registerListener(this, PlayerDisconnectListener(this.playerService))
        pluginManager.registerListener(this, PlayerPreConnectListener(this))
        pluginManager.registerListener(this, PlayerBlazingPackListener(this))

        pluginManager.registerCommand(this, GlobalInfoCommand(this.playerRepository))
        pluginManager.registerCommand(this, GlobalKickCommand())
        pluginManager.registerCommand(this, GlobalClearCommand(this.playerService))
        pluginManager.registerCommand(this, GlobalUnbugCommand(this.playerService, this.playerRepository))

        RedisAPI.getCommands {
            it.hgetall("pvpc-connect-players").forEach { (_, player) ->
                if (player is Player) {
                    if (player.proxyId != NetworkAPI.id) {
                        this.playerRepository.playerMap[player.uuid] = player
                    } else {
                        it.hdel("pvpc-connect-players", player.uuid.toString())
                        this.playerService.deletePlayer(player.uuid)
                    }
                }
            }
        }

        val executor = Executors.newScheduledThreadPool(1, ThreadFactoryBuilder().setNameFormat("Player Connect Thread").build())
        executor.scheduleWithFixedDelay(PlayerConnectTask(this.playerRepository, this.playerService), 0, 5, TimeUnit.SECONDS)

        ConnectAPI.playerRepository = this.playerRepository
    }

    override fun onDisable() {
        RedisAPI.getCommands {
            it.hgetall("pvpc-connect-players").forEach { (uuid, _) ->
                it.hdel("pvpc-connect-players", uuid)
            }
        }
    }

}