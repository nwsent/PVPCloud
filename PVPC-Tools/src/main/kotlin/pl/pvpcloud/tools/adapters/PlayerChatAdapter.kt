package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketPlayerChat

class PlayerChatAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerChat && id != NetworkAPI.id) {
            Bukkit.getOnlinePlayers()
                    .filter { it.hasPermission("chat.bypass.all") }
                    .forEach {
                        it.sendFixedMessage("&8[&c${id}&8] &7${packet.sender} &8» &r&7${packet.message}")
                    }
        }
    }

}