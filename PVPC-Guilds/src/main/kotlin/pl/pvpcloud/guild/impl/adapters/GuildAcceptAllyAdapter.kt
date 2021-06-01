package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.packet.PacketGuildAcceptAlly
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildAcceptAllyAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildAcceptAlly) {
            val targetGuild = this.guildRepository.getGuildById(packet.target)
                    ?: return
            val senderGuild = this.guildRepository.getGuildById(packet.sender)
                    ?: return

            Bukkit.getOnlinePlayers()
                    .forEach { it.sendFixedMessage("&bSojusz &8&l* &6${senderGuild.tag.toUpperCase()} &fzawarła sojusz z: &6${targetGuild.tag}") }
        }
    }

}