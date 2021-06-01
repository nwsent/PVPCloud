package pl.pvpcloud.auth.basic

import pl.pvpcloud.database.api.DatabaseAPI
import java.util.concurrent.ConcurrentHashMap

class AuthRepository {

    val playersMap = ConcurrentHashMap<String, AuthPlayer>()

    init {
        DatabaseAPI.loadAll<AuthPlayer>("auth-players") {
            it.forEach { player ->
                player.isLogged = false
                this.playersMap[player.name] = player
            }
        }
    }

    fun createPlayer(name: String): AuthPlayer =
            AuthPlayer(name).also {
                it.insertEntity()
                this.playersMap[name] = it
            }

    fun deletePlayer(player: AuthPlayer) =
            player.also {
                this.playersMap.remove(it.name)
            }.apply {
                this.deleteEntity()
            }

    fun addPlayer(player: AuthPlayer) {
        this.playersMap[player.name] = player
    }

    fun getAuthPlayer(name: String): AuthPlayer? =
            this.playersMap[name]
}