package pl.pvpcloud.guild.impl.variable.profil

import org.bukkit.entity.Player
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.tablist.TablistAdapter

class GuildKillsVariable(name: String) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        val guild = GuildsAPI.getGuildByMember(player.uniqueId)
                ?: return ""

        return " &8* &7Zabojstwa: &f${guild.kills}"
    }

}