package pl.pvpcloud.tools.adapters

import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.*

class PunishmentUpdateAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (id != NetworkAPI.id) {
            when (packet) {
                is PacketBanAdd -> {
                    this.plugin.banManager._bans.add(packet.ban)
                }
                is PacketBlacklistAdd -> {
                    this.plugin.blacklistManager.blacklists[packet.blacklist.name] = packet.blacklist
                }
                is PacketMuteAdd -> {
                    this.plugin.muteManager._mutes.add(packet.mute)
                }
                is PacketBanRemove -> {
                    this.plugin.banManager._bans.remove(packet.ban)
                }
                is PacketBlacklistRemove -> {
                    this.plugin.blacklistManager.blacklists.remove(packet.blacklist.name)
                }
                is PacketMuteRemove -> {
                    this.plugin.muteManager._mutes.remove(packet.mute)
                }
            }
        }
    }
}