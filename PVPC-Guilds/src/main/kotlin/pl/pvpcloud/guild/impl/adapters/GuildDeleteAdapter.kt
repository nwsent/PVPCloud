package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.guild.api.packet.PacketGuildDelete
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildDeleteAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildDelete) {
            // zamiana bo nie usunie gildii
            this.guildRepository.guildsMap.remove(packet.guildId)

            val leader = ConnectAPI.getPlayerByUUID(packet.leaderUUID)
                    ?: throw NullPointerException("guild is created, but cached leader is null")

            Bukkit.getOnlinePlayers()
                    .forEach { it.sendFixedMessage("&eGildia &8&l* &8(&a${packet.tag}&8) &fzostala usunieta przez&8: &e${leader.nick}&8.") }
        }
    }

}