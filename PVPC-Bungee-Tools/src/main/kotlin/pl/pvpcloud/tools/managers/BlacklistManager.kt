package pl.pvpcloud.tools.managers

import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.Blacklist
import pl.pvpcloud.tools.packets.PacketBlacklistAdd
import pl.pvpcloud.tools.packets.PacketBlacklistRemove
import java.util.concurrent.ConcurrentHashMap

class BlacklistManager(private val plugin: ToolsPlugin) {

    val blacklists = ConcurrentHashMap<String, Blacklist>()

    val valid = ByteArray(0).contentToString()

    init {
        DatabaseAPI.loadAll<Blacklist>("blacklists") { bl ->
            bl.forEach {
                this.blacklists[it.name] = it
            }
        }
    }

    fun getBlacklistName(name: String) = blacklists[name]

    fun getBlacklistIp(ip: String) = blacklists.values.singleOrNull { it.ip === ip }

    fun getBlacklistComputerUUID(uuid: ByteArray) = blacklists.values.filter { it.computerUUID.contentToString() != valid }.singleOrNull { it.computerUUID.contentToString() == uuid.contentToString() }

    fun createBlacklist(blacklist: Blacklist) {
        blacklist.insertEntity()
        this.blacklists[blacklist.name] = blacklist
        NetworkAPI.publish { PacketBlacklistAdd(blacklist) }
    }

    fun removeBlacklist(blacklist: Blacklist) {
        blacklist.deleteEntity()
        this.blacklists.remove(blacklist.name)
        NetworkAPI.publish { PacketBlacklistRemove(blacklist) }
    }

}