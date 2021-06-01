package pl.pvpcloud.hub.variable

import org.bukkit.entity.Player
import pl.pvpcloud.hub.HubPlugin
import pl.pvpcloud.tablist.TablistAdapter

class PlayersOnServerVariable(private val plugin: HubPlugin, name: String) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        return this.plugin.modeManager.getMode(name.replace("players-", ""))!!.online.toString()
    }

}