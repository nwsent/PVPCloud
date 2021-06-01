package pl.pvpcloud.tools.managers

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.User
import pl.pvpcloud.tools.packets.PacketPlayerUpdate
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class UserManager(private val plugin: ToolsPlugin) {

    private val userMap = ConcurrentHashMap<UUID, User>()

    /*init {
        DatabaseAPI.removeCollection("tools-users")
        /*DatabaseAPI.loadAll<User>("tools-users") {
            it.forEach { user ->
                this.userMap[user.uuid] = user
         }*/
    }*/

    fun createUser(uuid: UUID, name: String): User =
            User(uuid, name, System.currentTimeMillis(), System.currentTimeMillis()).also {
                this.userMap[it.uuid] = it
                it.insertEntity()
                NetworkAPI.publish { PacketPlayerUpdate(it) }
            }

    fun addUser(user: User) {
        this.userMap[user.uuid] = user
    }

    fun getUserByUUID(uuid: UUID): User? {
        return this.userMap[uuid]
    }

    fun getUserByNick(nick: String): User? {
        return this.userMap.values.singleOrNull { it.name == nick }
    }

    fun findUserByUUID(uniqueId: UUID): User {
        return this.userMap[uniqueId]
                ?: throw NullPointerException("$uniqueId id null")
    }

}