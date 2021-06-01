package pl.pvpcloud.guild.impl.adapters

import pl.pvpcloud.guild.api.packet.PacketGuildRecruitment
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter

class GuildRecruitmentAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildRecruitment) {

        }
    }

}