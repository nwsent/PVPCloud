package pl.pvpcloud.guild.impl.listener

import net.minecraft.server.v1_8_R3.PacketDataSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendPacket
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.guild.impl.GuildsBootstrap
import pl.pvpcloud.nats.NetworkAPI
import java.util.concurrent.TimeUnit

class GuildJoinListener(private val plguin: GuildsBootstrap) : Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val senderGuild = this.plguin.guildRepository.getGuildByMember(player.uniqueId) ?: return
        if (NetworkAPI.id == "lobby") {
            if (senderGuild.timeGuild - System.currentTimeMillis() < TimeUnit.DAYS.toMillis(3)) {
                player.sendFixedMessage(arrayListOf(
                        " &4UWAGA! &cTwoja gildie wygaśnie za: ${DataHelper.parseLong(senderGuild.timeGuild - System.currentTimeMillis(), false)}",
                        "    &8* &cPrzedłuż ją za pomocą komendy: &f/g przedluz"
                ))
            }
        }

        player.sendPacket(
                PacketPlayOutCustomPayload(this.plguin.guildsMessageRecruitmentTask.packetBP, PacketDataSerializer(this.plguin.guildsMessageRecruitmentTask.messageGUILD.slice()))
        )
    }

}