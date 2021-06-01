package pl.pvpcloud.guild.api.structure

import java.io.Serializable
import java.util.*

interface GuildMember : Serializable {

    val uuid: UUID
    val name: String
    val permissions: MutableSet<String>

    fun hasPermission(permission: String): Boolean = this.permissions.contains(permission)

}