package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.packet.PacketGuildChat
import pl.pvpcloud.guild.api.packet.PacketGuildChatAlly
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildChatAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildChat) {
            packet.guild.members
                    .mapNotNull { Bukkit.getPlayer(it.uuid) }
                    .forEach {
                        it.sendFixedMessage(" &8(&aGildia&8) &7${Bukkit.getPlayer(packet.uuid).name}&8: &f${packet.message}")
                    }

            Bukkit.getOnlinePlayers().forEach {
                val user = ToolsAPI.findUserByUUID(it)
                if (user.settings.socialSpy)
                    it.sendFixedMessage("&4&lGuildSpy &8(${packet.guild.tag}&8) &7${Bukkit.getPlayer(packet.uuid).name}&8: &f${packet.message}")
            }
        }
    }

}