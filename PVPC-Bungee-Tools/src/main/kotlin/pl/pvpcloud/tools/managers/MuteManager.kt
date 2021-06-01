package pl.pvpcloud.tools.managers

import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.Mute
import pl.pvpcloud.tools.packets.PacketMuteAdd
import pl.pvpcloud.tools.packets.PacketMuteRemove

class MuteManager(private val plugin: ToolsPlugin) {

    val _mutes = arrayListOf<Mute>()
    private val mutes get() = _mutes.filter { !it.expired }

    init {
        DatabaseAPI.loadAll<Mute>("mutes") {
            it.filter { it.expired }.forEach { it.deleteEntity() }
            val mute = it.filter { !it.expired }
            _mutes.addAll(mute)
        }
    }

    fun getMute(name: String) = mutes.singleOrNull { it.name == name }

    private fun createMute(name: String, source: String, time: Long, reason: String) = Mute(name, System.currentTimeMillis(), time, reason, source).also {
        it.insertEntity()
        _mutes.add(it)
        NetworkAPI.publish { PacketMuteAdd(it) }
    }

    fun removeMute(mute: Mute) {
        mute.deleteEntity()
        _mutes.remove(mute)
        NetworkAPI.publish { PacketMuteRemove(mute) }
    }

    fun mutePlayer(name: String, source: String, time: Long, reason: String) = createMute(
            name,
            source,
            time,
            reason
    )

}