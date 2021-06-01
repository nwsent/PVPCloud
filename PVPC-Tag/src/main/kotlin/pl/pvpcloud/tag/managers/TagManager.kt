package pl.pvpcloud.tag.managers

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
import net.minecraft.server.v1_8_R3.Scoreboard
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.helpers.ReflectionHelper
import pl.pvpcloud.tag.TagPlugin
import pl.pvpcloud.tag.event.PlayerChangeTag

class TagManager(private val plugin: TagPlugin) {

    private val scoreboard = Scoreboard()

    fun createPlayer(player: Player) {
        val name = player.name
        if (player.name === "KHcodepl") return
        val team = scoreboard.createTeam(name).apply {
            displayName = ""
            prefix = ""
            suffix = ""
        }
        scoreboard.addPlayerToTeam(name, name)
        val packet = PacketPlayOutScoreboardTeam(team, 0)
        for (it in Bukkit.getOnlinePlayers()) {
            if (player.name === "KHcodepl") continue
            ReflectionHelper.sendPacket(it, packet)
        }
        for (it in Bukkit.getOnlinePlayers()
                .minus(player)
                .mapNotNull { scoreboard.getPlayerTeam(it.name) }
                .map { PacketPlayOutScoreboardTeam(it, 0) }) {
            if (player.name === "KHcodepl") continue
            ReflectionHelper.sendPacket(player, it)
        }
    }


    fun updatePlayer(player: Player) {
        val team = scoreboard.getPlayerTeam(player.name) ?: return
        for (it in Bukkit.getOnlinePlayers()) {
            if (it.name === "KHcodepl") continue
            val event = PlayerChangeTag(player, it, true, "", "")
            Bukkit.getPluginManager().callEvent(event)
            if (event.canSee) {
                team.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS
            } else {
                team.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.NEVER
            }
            team.prefix = event.prefix.fixColors()
            team.suffix = event.suffix.fixColors()
            ReflectionHelper.sendPacket(it, PacketPlayOutScoreboardTeam(team, 2))
        }
    }

    fun removeBoard(player: Player) {
        val name = player.name
        val team = scoreboard.getPlayerTeam(name) ?: return
        scoreboard.removePlayerFromTeam(name, team)
        val packet = PacketPlayOutScoreboardTeam(team, 1)
        Bukkit.getOnlinePlayers().forEach {
            ReflectionHelper.sendPacket(it, packet)
        }
        Bukkit.getOnlinePlayers()
                .minus(player)
                .mapNotNull { PacketPlayOutScoreboardTeam(scoreboard.getPlayerTeam(it.name), 1) }
                .forEach { ReflectionHelper.sendPacket(player, it) }
        scoreboard.removeTeam(team)
    }
}