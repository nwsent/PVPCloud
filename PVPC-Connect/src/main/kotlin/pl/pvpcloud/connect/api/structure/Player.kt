package pl.pvpcloud.connect.api.structure

import java.io.Serializable
import java.util.*

interface Player : Serializable {

    val uuid: UUID
    val nick: String

    val proxyId: String
    var serverId: String

    var ip: String?
    var computerId: ByteArray?

    fun sendMessage(message: String)
    fun sendActionbar(message: String)
    fun sendTitle(title: String, subtitle: String)

    fun connect(id: String)
    fun kick(message: String)

}