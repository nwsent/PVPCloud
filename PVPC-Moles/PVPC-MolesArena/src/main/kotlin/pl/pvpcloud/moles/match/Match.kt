package pl.pvpcloud.moles.match

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.arena.Arena
import pl.pvpcloud.statistics.StatsAPI
import java.util.concurrent.ConcurrentHashMap

class Match(
    val plugin: MolesPlugin,
    val matchId: Int,
    val arena: Arena,
    val attackTeam: MatchTeam,
    val defendTeam: MatchTeam
) {
    var matchState = MatchState.WAITING
    var timeToEnd: Long = 0
    var countdown = 4
    private val votes = ConcurrentHashMap<String, Int>()
    var winKitProfileName: String = ""

    init {
        this.plugin.kitManager.kits.forEach {
            this.votes[it.name] = 0
        }
    }

    fun handleDeath(deadPlayer: Player, disconnect: Boolean) {
        if (disconnect) {
            this.sendMessage(this.plugin.config.messages.matchPlayerQuit.rep("%name%", deadPlayer.name))
        } else {
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                this.plugin.spectateManager.addSpectate(deadPlayer)
            }, 3L)
        }
        this.plugin.matchManager.playerMatchTeamId.remove(deadPlayer.uniqueId)
        this.getMatchTeamByPlayer(deadPlayer).killPlayer(deadPlayer.uniqueId)
        StatsAPI.incrementValue(deadPlayer.uniqueId, 2)
    }

    fun isState(matchState: MatchState): Boolean {
        return this.matchState == matchState
    }

    fun handleVote(kitName: String) {
        val actual = this.votes[kitName]!! + 1
        this.votes[kitName] = actual
    }

    fun handleEndVote() {
        this.winKitProfileName = this.votes.maxBy { it.value }!!.key
    }

    fun getVotes(kitName: String): Int {
        return this.votes[kitName] ?: 0
    }

    fun decrementCountdown(): Int {
        return --this.countdown
    }

    fun getOpponentTeam(player: Player): MatchTeam {
        return if (this.attackTeam.contains(player)) this.defendTeam else this.attackTeam
    }

    fun getPlayersMatch(): Sequence<Player> {
        return (getPlayersAttackAlive() + getPlayersDefendAlive())
    }

    fun getPlayersAttackAlive(): Sequence<Player> {
        return this.attackTeam.getAlivePlayers()
    }

    fun getPlayersDefendAlive(): Sequence<Player> {
        return this.defendTeam.getAlivePlayers()
    }

    fun getMatchTeamByPlayer(player: Player): MatchTeam {
        return if (this.getPlayersAttackAlive().contains(player)) this.attackTeam else this.defendTeam
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