package pl.pvpcloud.guild.impl.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.guild.api.packet.PacketGuildChat
import pl.pvpcloud.guild.api.packet.PacketGuildChatAlly
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.NetworkAPI

class GuildChatListener(private val guildRepository: GuildRepository) : Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message

        val guild = this.guildRepository.getGuildByMember(player.uniqueId)
                ?: return

        if (message.startsWith("!!")) {
            NetworkAPI.publish { PacketGuildChatAlly(message.rep("!!", ""), guild, player.uniqueId) }
            event.isCancelled = true
        }
        else if (message.startsWith("!")) {
            NetworkAPI.publish { PacketGuildChat(message.rep("!", ""), guild, player.uniqueId) }
            event.isCancelled = true
        }
    }

}