package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.guild.api.packet.PacketGuildRemove
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.tools.ToolsAPI

class GuildRemoveAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildRemove) {
            val guild = this.guildRepository.getGuildById(packet.guildId)
                    ?: throw NullPointerException("guild is removed by leader, but guild is not exits, wtf")

            val member = ConnectAPI.getPlayerByUUID(packet.uuid)
                    ?: throw NullPointerException("member is removed, but he is not exists in database, wtf")

            Bukkit.getOnlinePlayers()
                    .forEach {
                        if (packet.whoRemoved != packet.uuid) {
                            val whoRemove = ConnectAPI.getPlayerByUUID(packet.whoRemoved)
                                    ?: throw NullPointerException("member is removed, but idk who added he, wtf")

                            it.sendFixedMessage("&eGildia &8&l* &fGracz&8: &6${member.nick} &fzostal wyrzucony z gildii &8(&a${guild.tag}&8) &fprzez&8: &6${whoRemove.nick}&8.")
                        } else {
                            it.sendFixedMessage("&eGildia &8&l* &fGracz&8: &6${member.nick} &fopuscil gildie &8(&a${guild.tag}&8)")
                        }
                    }
        }
    }

}