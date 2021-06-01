package pl.pvpcloud.connect.api.structure

import java.util.*

interface PlayerRepository {

    val playerMap: MutableMap<UUID, Player>

    fun getPlayerBy(block: (Player) -> Boolean): Player?
    fun getPlayerByUUID(uuid: UUID): Player?
    fun getPlayerByNick(nick: String): Player?

}