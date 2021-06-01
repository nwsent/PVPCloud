package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.packets.chat.PacketPlayerHelpOp
import pl.pvpcloud.tools.ToolsPlugin

class HelpOpAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketPlayerHelpOp) {
            Bukkit.getOnlinePlayers()
                    .filter { it.hasPermission("helpop.bypass") }
                    .forEach {
                        it.sendMessage(" ")
                        it.sendFixedMessage(" &4&lHelpop &8(&c&l${packet.serverId}&8) &8&l» &7${packet.name}&8: &f${packet.message}")
                        it.sendMessage(" ")
                    }
        }
    }

}