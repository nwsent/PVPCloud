package pl.pvpcloud.tools.managers

import net.md_5.bungee.api.connection.ProxiedPlayer
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.Ban
import pl.pvpcloud.tools.basic.BanType
import pl.pvpcloud.tools.packets.PacketBanAdd
import pl.pvpcloud.tools.packets.PacketBanRemove

class BanManager(private val plugin: ToolsPlugin) {

    val _bans = arrayListOf<Ban>()
    private val bans get() = _bans.filter { !it.expired }
    private val ipBans get() = bans.filter { it.type == BanType.IP }
    private val idBans get() = bans.filter { it.type == BanType.PLAYER }

    init {
        DatabaseAPI.loadAll<Ban>("bans") {
            it.filter { it.expired }.forEach { it.deleteEntity() }
            val bans = it.filter { !it.expired }
            _bans.addAll(bans)
        }
    }

    fun getBan(name: String, type: BanType) = if (type == BanType.IP) ipBans.singleOrNull { it.name == name } else idBans.singleOrNull { it.name == name }

    private fun createBan(name: String, source: String, type: BanType, time: Long, reason: String) = Ban(name, type, System.currentTimeMillis(), time, reason, source).also {
        it.insertEntity()
        _bans.add(it)
        NetworkAPI.publish { PacketBanAdd(it) }
    }

    fun removeBan(ban: Ban) {
        ban.deleteEntity()
        _bans.remove(ban)
        NetworkAPI.publish { PacketBanRemove(ban) }
    }

    fun banIp(p: ProxiedPlayer, source: String, time: Long, reason: String) = createBan(
            p.address.address.hostAddress,
            source,
            BanType.IP,
            time,
            reason
    )

    fun banId(name: String, source: String, time: Long, reason: String) = createBan(
            name,
            source,
            BanType.PLAYER,
            time,
            reason
    )

    fun kickPlayerAfterBan(playerName: String, ban: Ban) {
        val playerCache = ConnectAPI.getPlayerByNick(playerName) ?: return
        val message = arrayListOf(*plugin.config.messages.formatLoginDisallowBanned.toTypedArray())
        message.replaceAll { ban.replaceString(it) }
        playerCache.kick(message.joinToString(separator = "\n").fixColors())
    }
}