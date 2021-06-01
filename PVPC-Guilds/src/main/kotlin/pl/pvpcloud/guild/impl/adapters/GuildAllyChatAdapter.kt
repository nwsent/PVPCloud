package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.packet.PacketGuildChatAlly
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildAllyChatAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildChatAlly) {
            packet.guild.also { guild ->
                guild.members
                        .mapNotNull { Bukkit.getPlayer(it.uuid) }
                        .forEach{
                            it.sendFixedMessage(" &8(&bSojusz&8) &f${packet.guild.tag} &7${Bukkit.getPlayer(packet.uuid).name}&8: &f${packet.message}")
                        }
                guild.allies.forEach { ally ->
                    val guildAlly = GuildsAPI.getGuildById(ally)
                            ?: return
                    guildAlly.members
                            .mapNotNull { Bukkit.getPlayer(it.uuid) }
                            .forEach {
                                it.sendFixedMessage(" &8(&bSojusz&8) &f${packet.guild.tag} &7${Bukkit.getPlayer(packet.uuid).name}&8: &f${packet.message}")
                            }
                }
            }
            Bukkit.getOnlinePlayers().forEach {
                val user = ToolsAPI.findUserByUUID(it)
                if (user.settings.socialSpy) {
                    it.sendFixedMessage("&4&lAllySpy &8(${packet.guild.tag}&8) &7${Bukkit.getPlayer(packet.uuid).name}&8: &f${packet.message}")
                }
            }
        }

    }

}