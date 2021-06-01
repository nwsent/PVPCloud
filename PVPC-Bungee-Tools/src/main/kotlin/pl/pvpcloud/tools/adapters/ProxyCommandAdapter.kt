package pl.pvpcloud.tools.adapters

import net.md_5.bungee.api.ProxyServer
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketProxyCommand

class ProxyCommandAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketProxyCommand) {
            ProxyServer.getInstance().pluginManager.dispatchCommand(ProxyServer.getInstance().console, packet.command)
        }
    }
}