package pl.pvpcloud.castle.match

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.arena.Arena
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.statistics.StatsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Match(
    val plugin: CastlePlugin,
    val arena: Arena,
    val attackTeam: MatchTeam,
    val defendTeam: MatchTeam,
    val matchId: UUID
) {
    var matchState = MatchState.VOTING
    var timeToEnd: Long = 0
    var countdown = 10
    val votes = ConcurrentHashMap<String, Int>()
    var winKitName: String = ""
    var hp = 200
    var attacking = false
    var lastExplode: Long = 0
    var lastPlaceTnt: Long = 0
    var canBuildAndDestroy: Long = 0 //moze kiedys

    init {
        this.plugin.saveModule.saveConfig.sections.forEach {
            this.votes[it.kitName] = 0
        }
    }

    fun handleDeath(deadPlayer: Player, disconnect: Boolean) {
        if (disconnect) {
            this.sendMessage(this.plugin.config.messages.matchPlayerQuit.rep("%name%", deadPlayer.name))
        } else {
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                this.plugin.spectateManager.addSpectate(deadPlayer, this, true)
            }, 3L)
        }
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
        this.winKitName = this.votes.maxBy { it.value }!!.key
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