package pl.pvpcloud.grouptp.arena.match

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.arena.arena.Arena
import pl.pvpcloud.statistics.StatsAPI
import java.util.*
import kotlin.collections.HashSet

class Match(
        val plugin: GroupTpPlugin,
        val matchId: Int,
        val arena: Arena,
        val players: HashSet<UUID>,
        val kitName: String
) {
    var matchState = MatchState.STARTING
    var timeToEnd: Long = 0
    var countdown = 4

    fun handleDeath(deadPlayer: Player, disconnect: Boolean) {
        if (disconnect) {
            this.sendMessage(this.plugin.config.messages.matchPlayerQuit.rep("%name%", deadPlayer.name))
        }
        this.players.remove(deadPlayer.uniqueId)
        val connectPlayer = ConnectAPI.getPlayerByUUID(deadPlayer.uniqueId)
                ?: return
        connectPlayer.connect("gtp_hub")
    }

    fun isState(matchState: MatchState): Boolean {
        return this.matchState == matchState
    }

    fun decrementCountdown(): Int {
        return --this.countdown
    }

    fun getPlayersMatch(): Sequence<Player> {
        return this.players.asSequence().mapNotNull { Bukkit.getPlayer(it) }.filter { it.gameMode == GameMode.SURVIVAL }
    }

    fun getAlivePlayersCount(): Int {
        return this.getPlayersMatch().count()
    }

    fun getPlayersWorld(): Sequence<Player> {
        return this.arena.world.players.asSequence()
    }

    fun sendTitle(title: String, subTitle: String) {
        this.getPlayersWorld().forEach {
            it.sendTitle(title, subTitle, 0, 60, 0)
        }
    }

    fun sendMessage(message: String) {
        this.getPlayersWorld().forEach { it.sendFixedMessage(message) }
    }
}