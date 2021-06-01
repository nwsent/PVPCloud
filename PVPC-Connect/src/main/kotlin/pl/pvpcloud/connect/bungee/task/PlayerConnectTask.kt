package pl.pvpcloud.connect.bungee.task

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import pl.pvpcloud.connect.api.structure.PlayerRepository
import pl.pvpcloud.connect.api.structure.PlayerService
import pl.pvpcloud.nats.NetworkAPI

class PlayerConnectTask(private val playerRepository: PlayerRepository, private val playerService: PlayerService) : Runnable {

    override fun run() {
        for (player in ProxyServer.getInstance().players) {
            val cachedPlayer = this.playerRepository.getPlayerByUUID(player.uniqueId)

            if (cachedPlayer == null) {
                player.disconnect(TextComponent("Zostales wyrzucony, poniewaz nie jestes w bazie danych!"))
                return
            }
        }

        for (cachedPlayer in this.playerRepository.playerMap.values) {
            val player = ProxyServer.getInstance().getPlayer(cachedPlayer.uuid)

            if (player == null && cachedPlayer.proxyId == NetworkAPI.id) {
                this.playerService.deletePlayer(cachedPlayer.uuid)
            }
        }
    }

}