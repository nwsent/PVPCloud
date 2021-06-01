package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.packets.chat.PacketChatClear
import pl.pvpcloud.packets.chat.PacketChatSwitchLock
import pl.pvpcloud.tools.ToolsPlugin

class ChatAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    private val cleanMessage = arrayOfNulls<String>(100).also { it.fill("") }

    override fun received(id: String, packet: Any) {
        if (packet is PacketChatClear) {
            Bukkit.getOnlinePlayers()
                    .filter { !it.hasPermission("tools.chat.show") }
                    .forEach {
                        it.sendMessage(cleanMessage)
            }
            Bukkit.broadcastMessage("&8* &7Chat zostal&8: &dwyczyszczony &7przez&8: &e${packet.adminName} &8*".fixColors())
        }
        if (packet is PacketChatSwitchLock) {
                Bukkit.getOnlinePlayers()
                        .filter { !it.hasPermission("tools.chat.show") }
                        .forEach {
                            it.sendMessage(cleanMessage)
                        }
            Bukkit.broadcastMessage("&8* &7Chat zostal&8: ${if (packet.lock) "&cwylaczony" else "&awlaczony"} &7przez&8: &e${packet.adminName} &8*".fixColors())

        }
    }

}