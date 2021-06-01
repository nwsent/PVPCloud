package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.guild.api.packet.PacketGuildCreate
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildCreateAdapter : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildCreate) {
            val leader = ConnectAPI.getPlayerByUUID(packet.leaderUUID)
                    ?: throw NullPointerException("guild is created, but cached leader is null")

            Bukkit.getOnlinePlayers()
                    .forEach { it.sendFixedMessage("&eGildia &8&l* &8(&a${packet.tag}&8) &fzostala zalozona przez&8: &e${leader.nick}&8.") }
        }
    }

}