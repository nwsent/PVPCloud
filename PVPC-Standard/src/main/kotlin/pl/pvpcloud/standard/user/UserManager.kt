package pl.pvpcloud.standard.user

import pl.pvpcloud.standard.StandardPlugin
import java.util.*

class UserManager(private val plugin: StandardPlugin) {

    private val user = hashMapOf<UUID, UserProfil>()

    fun addUser(uuid: UUID) =
            user.put(uuid, UserProfil(uuid))

    fun getUsers() = user

    fun removeUser(uuid: UUID) = user.remove(uuid)

    fun findUser(uuid: UUID) = user[uuid]
            ?: throw NullPointerException("player is null $uuid")

}