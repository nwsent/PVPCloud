package pl.pvpcloud.connect.common.structure

import pl.pvpcloud.connect.api.structure.Player
import pl.pvpcloud.connect.api.structure.PlayerRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

class PlayerRepositoryImpl : PlayerRepository {

    override val playerMap: MutableMap<UUID, Player> = ConcurrentHashMap()

    override fun getPlayerBy(block: (Player) -> Boolean): Player? {
        return this.playerMap.values.find(block)
    }

    override fun getPlayerByUUID(uuid: UUID): Player? {
        return this.playerMap[uuid]
    }

    override fun getPlayerByNick(nick: String): Player? {
        return this.getPlayerBy { it.nick == nick }
    }

}