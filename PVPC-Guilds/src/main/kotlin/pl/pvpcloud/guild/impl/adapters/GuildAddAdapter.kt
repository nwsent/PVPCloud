package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.guild.api.packet.PacketGuildAdd
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildAddAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildAdd) {
            val guild = this.guildRepository.getGuildById(packet.guildId)
                    ?: throw NullPointerException("guild is add to guild, but guild is not exits, wtf")

            val member = ConnectAPI.getPlayerByUUID(packet.uuid)
                    ?: throw NullPointerException("member is added, but he is not exists in database, wtf")

            Bukkit.getOnlinePlayers()
                    .forEach { it.sendFixedMessage("&eGildia &8&l* &fGracz&8: &6${member.nick} &fdolaczyl do gildii &8(&a${guild.tag}&8)") }

        }
    }

}