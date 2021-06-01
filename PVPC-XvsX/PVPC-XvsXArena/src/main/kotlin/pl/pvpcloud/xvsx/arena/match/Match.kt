package pl.pvpcloud.xvsx.arena.match

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.arena.arena.Arena
import pl.pvpcloud.xvsx.common.kit.Kit

class Match(
        val plugin: XvsXPlugin,
        val id: Int,
        val kit: Kit,
        var round: Int,
        val playersSize: Int,
        val arena: Arena
) {
    var matchState: MatchState = MatchState.STARTING

    var countdown = 4

    val winTeam = -1

    init {
        MatchTask(this)
    }

    fun decrementCountdown(): Int {
        return --this.countdown
    }

    fun getPlayersMatch(): Sequence<Player> {
        return this.plugin.profileManager
    }

    fun getAlivePlayersCount(): Int {
        return this.getPlayersMatch().count()
    }

    fun sendTitle(subtitle: String) {
        this.getPlayersMatch().forEach { it.sendTitle("", subtitle, 5, 30, 5) }
    }
}