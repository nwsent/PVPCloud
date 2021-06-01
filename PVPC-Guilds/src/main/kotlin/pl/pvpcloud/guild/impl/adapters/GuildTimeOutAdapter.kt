package pl.pvpcloud.guild.impl.adapters

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.guild.api.packet.PacketGuildTimeOut
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.nats.api.INatsAdapter

class GuildTimeOutAdapter(private val guildRepository: GuildRepository) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketGuildTimeOut) {
            this.guildRepository.guildsMap.remove(packet.guild.guildId)
            Bukkit.getOnlinePlayers().forEach {
                it.sendFixedMessage("&eGildia &8&l* &8(&a${packet.guild.tag}&8) &fwygasła")
            }
        }
    }

}