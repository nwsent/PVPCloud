package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.packets.chat.PacketGlobalMessage
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalTitle

class GlobalMessageAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGlobalTitle) {
            Bukkit.getOnlinePlayers()
                    .forEach {
                        it.sendTitle(packet.title, packet.subtitle.rep("<3", "\u2764"), 10, 60, 10)
                    }
        }
        if (packet is PacketGlobalMessage) {
            if (packet.permission == "-") {
                Bukkit.broadcastMessage(packet.message)
            } else {
                Bukkit.broadcast(packet.message, packet.permission)
            }
        }

    }

}