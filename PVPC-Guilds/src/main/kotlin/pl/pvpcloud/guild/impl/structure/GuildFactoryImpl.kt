package pl.pvpcloud.guild.impl.structure

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.guild.api.event.default.GuildAddEvent
import pl.pvpcloud.guild.api.event.default.GuildCreateEvent
import pl.pvpcloud.guild.api.event.default.GuildDeleteEvent
import pl.pvpcloud.guild.api.event.default.GuildRemoveEvent
import pl.pvpcloud.guild.api.packet.*
import pl.pvpcloud.guild.api.structure.Guild
import pl.pvpcloud.guild.api.structure.GuildFactory
import pl.pvpcloud.guild.api.structure.GuildRepository
import pl.pvpcloud.guild.impl.GuildsPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.*

class GuildFactoryImpl(private val plugin: GuildsPlugin, private val guildRepository: GuildRepository) : GuildFactory {

    override fun createGuild(id: UUID, tag: String, name: String, leaderUUID: UUID, leaderName: String) {
        val guild = GuildImpl(id, tag, name, leaderUUID)
        guild.members.add(GuildMemberImpl(leaderUUID, leaderName))
        guild.insertEntity()

        NetworkAPI.publish { PacketGuildCreate(id, tag, name, leaderUUID) }
        NetworkAPI.publish { PacketGuildUpdate(guild) }
        Bukkit.getPluginManager().callEvent(GuildCreateEvent(guild))
    }

    override fun removeGuild(id: UUID, who: UUID): Boolean {
        val guild = this.guildRepository.getGuildById(id) as GuildImpl? ?: return false
        guild.deleteEntity()

        guild.allies.forEach { guildId ->
            val allyGuild = this.guildRepository.getGuildById(guildId) ?: return false //cos sie wyjebalo jak chuj
            allyGuild.allies.remove(guild.guildId)
            (allyGuild as GuildImpl).updateEntity()
            NetworkAPI.publish { PacketGuildUpdate(allyGuild) }
        }

        Bukkit.getPluginManager().callEvent(GuildDeleteEvent(guild))
        NetworkAPI.publish { PacketGuildDelete(id, guild.tag, guild.name, who) }
        return true
    }

    override fun timeOutGuild(guild: Guild): Boolean {
        (guild as GuildImpl).deleteEntity()

        guild.allies.forEach { guildId ->
            val allyGuild = this.guildRepository.getGuildById(guildId) ?: return false //cos sie wyjebalo jak chuj
            allyGuild.allies.remove(guild.guildId)
            (allyGuild as GuildImpl).updateEntity()
            NetworkAPI.publish { PacketGuildUpdate(allyGuild) }
        }

        Bukkit.getPluginManager().callEvent(GuildDeleteEvent(guild))
        NetworkAPI.publish { PacketGuildTimeOut(guild) }
        return true
    }

    override fun addMember(uuid: UUID, name: String, target: Guild): Boolean {
        val targetGuild = target as GuildImpl

        val member = GuildMemberImpl(uuid, name)
        targetGuild.members.add(member)
        targetGuild.updateEntity()

        Bukkit.getPluginManager().callEvent(GuildAddEvent(targetGuild, member))
        NetworkAPI.publish { PacketGuildAdd(targetGuild.guildId, uuid) }
        NetworkAPI.publish { PacketGuildUpdate(targetGuild) }
        return true
    }

    override fun inviteMember(uuid: UUID, target: Guild) {
        target.invites.add(uuid)
        (target as GuildImpl).updateEntity()

        NetworkAPI.publish { PacketGuildUpdate(target) }
    }

    override fun inviteAlly(sender: UUID, target: UUID) {
        val targetGuild = this.guildRepository.getGuildById(target)
                ?: return
        targetGuild.alliesInvites.add(sender)
        (targetGuild as GuildImpl).updateEntity()

        NetworkAPI.publish {
            PacketGuildUpdate(targetGuild)
        }

    }

    override fun acceptAlly(sender: UUID, target: UUID) {
        val targetGuild = this.guildRepository.getGuildById(target)
                ?: return
        val senderGuild = this.guildRepository.getGuildById(sender)
                ?: return

        targetGuild.allies.add(sender)
        senderGuild.allies.add(target)
        (targetGuild as GuildImpl).updateEntity()
        (senderGuild as GuildImpl).updateEntity()
        NetworkAPI.publish { PacketGuildAcceptAlly(sender, target) }
        NetworkAPI.publish { PacketGuildUpdate(senderGuild) }
        NetworkAPI.publish { PacketGuildUpdate(targetGuild) }
    }

    override fun removeAlly(sender: UUID, target: UUID) {
        val targetGuild = this.guildRepository.getGuildById(target)
                ?: return
        targetGuild.allies.remove(sender)
        (targetGuild as GuildImpl).updateEntity()
        NetworkAPI.publish { PacketGuildUpdate(targetGuild) }


        val senderGuild = this.guildRepository.getGuildById(sender)
                ?: return

        senderGuild.allies.remove(target)
        (senderGuild as GuildImpl).updateEntity()
        NetworkAPI.publish { PacketGuildUpdate(senderGuild) }

        NetworkAPI.publish { PacketGuildRemoveAlly(sender, target) }
    }


    override fun removeMember(uuid: UUID, target: Guild, who: UUID): Boolean {
        val members = target.members.iterator()

        while (members.hasNext()) {
            val member = members.next()

            if (member.uuid == uuid) {
                members.remove()

                (target as GuildImpl).updateEntity()
                Bukkit.getPluginManager().callEvent(GuildRemoveEvent(target, member, who))
                NetworkAPI.publish { PacketGuildRemove(target.guildId, uuid, who) }
                NetworkAPI.publish { PacketGuildUpdate(target) }
                return true
            }
        }
        return false
    }

    override fun getGuildTagFor(sender: Player, receiver: Player): String {
        val playerGuild = this.guildRepository.getGuildByMember(sender.uniqueId) ?: return ""
        if (playerGuild.isMember(receiver.uniqueId)) return " &8&l| &a${playerGuild.tag} "
        val otherGuild = this.guildRepository.getGuildByMember(receiver.uniqueId)

        return if (otherGuild == null || !playerGuild.isAlly(otherGuild)) {
            if (receiver.hasPermission("tools.bypass.incognito")) {
                " &8&l| &c${playerGuild.tag}"
            } else {
                " &8&l| &c${if (ToolsAPI.findUserByUUID(sender.uniqueId).settings.isIncognito) playerGuild.tag else "???"}"
            }
        } else {
            " &8&l| &9${playerGuild.tag} "
        }
    }

}