package pl.pvpcloud.guild.impl.task

import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.Packet
import net.minecraft.server.v1_8_R3.PacketDataSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.common.bossbar.BarColor
import pl.pvpcloud.common.bossbar.BarStyle
import pl.pvpcloud.common.bossbar.BossBarBuilder
import pl.pvpcloud.common.bossbar.BossBarPacket
import pl.pvpcloud.common.extensions.sendPacket
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.guild.impl.listener.GuildJoinListener
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class GuildsMessageRecruitmentTask(val plugin: GuildsPlugin, val guildRepository: GuildRepository) : BukkitRunnable() {


    private var guilds = this.guildRepository.guildRecruitment.iterator()

    val packetBP = "BP|UpdateBossInfo"
    val uuid: UUID = UUID.randomUUID()

    init {
        runTaskTimer(this.plugin, 0, 1)
    }

    val messageGUILD: PacketDataSerializer =  BossBarBuilder
            .add(uuid)
            .style(BarStyle.SEGMENTED_12)
            .color(BarColor.GREEN)
            .title(TextComponent.fromLegacyText("0 min"))
            .buildPacket().serialize() ?:throw NullPointerException("Jebac jave")

    override fun run() {
   /*     if (this.guildRepository.guildRecruitment.isEmpty()) return


        if (!guilds.hasNext()) {
            guilds = this.guildRepository.guildRecruitment.iterator()
        }
        val guild = this.guildRepository.getGuildById(guilds.next().key) ?: return



    */
        val pds = arrayOfNulls<PacketDataSerializer>(3)

        val system = System.currentTimeMillis()

        pds[0] = BossBarBuilder
                .updateProgress(uuid)
                .progress((sin(system / 3000.0) + 1.0).toFloat() / 2)
                .buildPacket().serialize()

        pds[1] = BossBarBuilder
                .updateTitle(uuid)
                .title(TextComponent.fromLegacyText("""
                    
                    !!! PVPCLOUD.PL - THE BEST SERVER IN THE WORLD !!!
                    
                """.trimIndent()))
                .buildPacket().serialize()
        
/*
        pds[1] = BossBarBuilder
                .updateTitle(this.packetUUID)
                .title(TextComponent.fromLegacyText("PVPCLOUD.PL"))
                .buildPacket().serialize()
 */


        pds.forEach {
            val packet = PacketPlayOutCustomPayload(packetBP, PacketDataSerializer(it!!.slice()))
            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendPacket(packet)
            }
        }

    }

}
