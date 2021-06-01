package pl.pvpcloud.hub.scoreboard

import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.common.helpers.PlayerHelper
import pl.pvpcloud.scoreboard.basic.ScoreboardProvider
import pl.pvpcloud.tools.ToolsAPI

class HubScoreboardProvider : ScoreboardProvider() {

    override fun getTitle(p: Player): String {
        return "&f&lPVP&e&lCloud.pl"
    }

    override fun getLines(p: Player): List<String> {
        val user = ToolsAPI.findUserByUUID(p.uniqueId)
        return arrayListOf(
                "",
                "&7Nick&8: &f${p.name}",
                "&7Ranga&8: &f${PlayerHelper.getGroupFriendlyName(PlayerHelper.getGroup(p.uniqueId))}",
                "&7Stan konta&8: &f${user.coins}",
                "",
                "&7Spedzony czas&8: &e${DataHelper.parseLong(user.timeSpent, true)}",
                "",
                " &e&l* &fLobby"
        )
    }
}