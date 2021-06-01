package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalCommand

class GlobalCommandAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGlobalCommand) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), packet.command)
        }
    }
}