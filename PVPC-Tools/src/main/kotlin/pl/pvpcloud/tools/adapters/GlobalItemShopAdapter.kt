package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalItemShop


class GlobalItemShopAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGlobalItemShop) {
            Bukkit.getOnlinePlayers()
                    .filter {
                        val user = this.plugin.userManager.getUserByUUID(it.uniqueId)
                                ?: return
                        user.settings.ignoreShopMessages
                    }
                    .forEach {
                        it.sendFixedMessage(packet.text)
                        it.sendTitle("", packet.title, 10, 60, 10)
                    }
        }

    }

}