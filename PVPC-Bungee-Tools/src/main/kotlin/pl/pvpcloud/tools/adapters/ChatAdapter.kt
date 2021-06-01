package pl.pvpcloud.tools.adapters

import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.packets.chat.PacketChatDelay
import pl.pvpcloud.packets.chat.PacketChatSwitchLock
import pl.pvpcloud.tools.ToolsPlugin

class ChatAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        when (packet) {
            is PacketChatSwitchLock -> {
                this.plugin.config.chatIsLock = packet.lock
            }
            is PacketChatDelay -> {
                plugin.config.delayTimeChat = packet.delayTime
            }
        }
    }
}