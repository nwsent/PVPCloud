package pl.pvpcloud.xvsx.hub.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.xvsx.hub.XvsXPlugin

class XvsXScoreboardProvider(private val plugin: XvsXPlugin) : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val user = ToolsAPI.findUserByUUID(p.uniqueId)

        return arrayListOf(
                "",
                "&7Nick&8: &f${p.name}",
                "&7Stan konta&8: &f${user.coins}",
                "",
                "&7Walczy&8: &f${ConnectAPI.getPlayersOnServer("xvsx_1").count()}",
                "",
                " &e&l* &fXvsX"
        )
    }
}