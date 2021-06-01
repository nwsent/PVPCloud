package pl.pvpcloud.tools.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketSocialSpyMessage

class SocialSpyAdapter(private val plugin: ToolsPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketSocialSpyMessage) {
            Bukkit.getOnlinePlayers()
                    .filter {
                        val user = this.plugin.userManager.getUserByUUID(it.uniqueId)
                                ?: return
                        user.settings.socialSpy
                    }
                    .forEach {
                    it.sendFixedMessage(packet.message)
                }
            }
        }

}