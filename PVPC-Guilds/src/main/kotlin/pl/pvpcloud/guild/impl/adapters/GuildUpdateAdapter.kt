package pl.pvpcloud.guild.impl.adapters

import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter

class GuildUpdateAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildUpdate) {
            this.guildRepository.guildsMap[packet.guild.guildId] = packet.guild
        }
    }

}