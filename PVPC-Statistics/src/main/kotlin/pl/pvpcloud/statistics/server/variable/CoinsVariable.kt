package pl.pvpcloud.statistics.server.variable

import org.bukkit.entity.Player
import pl.pvpcloud.tablist.TablistAdapter
import pl.pvpcloud.tools.ToolsAPI

class CoinsVariable(name: String) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        return ToolsAPI.findUserByUUID(player.uniqueId).coins.toString()
    }

}