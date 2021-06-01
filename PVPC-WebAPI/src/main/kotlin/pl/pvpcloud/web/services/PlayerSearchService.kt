package pl.pvpcloud.web.services

import com.google.gson.JsonElement
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.basic.User
import pl.pvpcloud.web.WebApplication
import pl.pvpcloud.web.api.UserResponse
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class PlayerSearchService(private val webApplication: WebApplication) {

    fun getPlayerInfo(name: String): JsonElement {
        val user = getUser(name) ?: return webApplication.gson.toJsonTree("404")

        val group = this.getGroup(user.uuid)
        return webApplication.gson.toJsonTree(UserResponse(
                user.name,
                user.uuid,
                user.coins,
                user.timeSpent,
                user.firstJoinTime,
                user.lastJoinTime,
                user.discord,
                user.discordKey,
                false,
                group
        ))
    }

    fun setDiscord(nick: String): User? {
        val future = CompletableFuture<User>()

        DatabaseAPI.loadBySelector<User>("tools-users", nick, "name") {
            it!!.discord = true
            it!!.discordKey = "Brak"
            it.updateEntity()
        }
        ToolsAPI.getUserByNick(nick)!!.discord = true
        ToolsAPI.getUserByNick(nick)!!.discordKey = "Brak"

        return future.get(1, TimeUnit.SECONDS)
    }

    fun getDiscordKey(discordKey: String): User? {
        val future = CompletableFuture<User>()

        DatabaseAPI.loadBySelector<User>("tools-users", discordKey, "discordKey") {
            future.complete(it)
        }
        return future.get(1, TimeUnit.SECONDS)
    }


    fun getUser(name: String): User? {
        val future = CompletableFuture<User>()
        DatabaseAPI.loadBySelector<User>("tools-users", name, "name") {
            future.complete(it)
        }
        return future.get(1, TimeUnit.SECONDS)
    }

    fun getGroup(uuid: UUID): String {
        val future = CompletableFuture<String>()
        future.complete(PlayerHelper.getGroupOffline(uuid))
        return future.get(1, TimeUnit.SECONDS)
    }
}