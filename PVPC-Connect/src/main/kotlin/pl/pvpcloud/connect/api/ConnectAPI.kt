package pl.pvpcloud.connect.api

import pl.pvpcloud.connect.api.structure.Player
import pl.pvpcloud.connect.api.structure.PlayerRepository
import java.util.*

interface ConnectAPI {

    companion object {
        internal lateinit var playerRepository: PlayerRepository

        fun getPlayerBy(block: (Player) -> Boolean): Player? = this.playerRepository.getPlayerBy(block)
        fun getPlayerByUUID(uuid: UUID): Player? = this.playerRepository.getPlayerByUUID(uuid)
        fun getPlayerByNick(nick: String): Player? = this.playerRepository.getPlayerByNick(nick)
        fun getPlayers() = this.playerRepository.playerMap
        fun getPlayersOnServer(serverId: String) = this.playerRepository.playerMap.values.filter { it.serverId == serverId }
        fun getPlayersOnServers(serverId: ArrayList<String>): Set<Player> {
            val players = hashSetOf<Player>()
            serverId.forEach { players.addAll(getPlayersOnServer(it)) }
            return players
        }
    }
}