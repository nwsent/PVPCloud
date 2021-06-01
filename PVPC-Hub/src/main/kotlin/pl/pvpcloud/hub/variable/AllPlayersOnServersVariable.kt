package pl.pvpcloud.hub.variable

import org.bukkit.entity.Player
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.tablist.TablistAdapter

class AllPlayersOnServersVariable(name: String) : TablistAdapter(name) {

    override fun replace(player: Player): String {
        return ConnectAPI.getPlayers().size.toString()
    }
}