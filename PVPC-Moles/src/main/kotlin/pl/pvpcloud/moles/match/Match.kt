package pl.pvpcloud.moles.match

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.arena.Arena
import pl.pvpcloud.moles.match.event.MatchEndEvent
import pl.pvpcloud.moles.team.TeamPlayer
import pl.pvpcloud.statistics.StatsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Match(
    val plugin: MolesPlugin,
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

    init {
        this.plugin.kitModule.kitManager.getKitsName().forEach {
            this.votes[it] = 0
        }
    }

    fun handleDeath(deadPlayer: Player, disconnect: Boolean) {
        val teamPlayer = this.getTeamPlayerByPlayer(deadPlayer)
        if (!teamPlayer.alive) return
        val deadMatchTeam = this.getMatchTeamByPlayer(deadPlayer).also {
            it.killPlayer(deadPlayer)
        }
        if (disconnect) {
            this.sendMessage(this.plugin.config.messages.matchPlayerQuit.rep("%name%", deadPlayer.name))
        } else {
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                this.plugin.spectateManager.addSpectate(deadPlayer, this, true)
            }, 3L)
        }
        StatsAPI.incrementValue(deadPlayer.uniqueId, 2)
        if (this.canEnd()) {
            this.plugin.server.pluginManager.callEvent(MatchEndEvent(getOpponentTeam(deadPlayer), deadMatchTeam, this))
        }
    }

    fun isState(matchState: MatchState): Boolean {
        return this.matchState == matchState
    }

    fun canEnd(): Boolean {
        val aliveA = this.attackTeam.getAliveCount()
        val aliveD = this.defendTeam.getAliveCount()
        if (aliveA == 0 && aliveD != 0 || aliveA != 0 && aliveD == 0) {
            return true
        }
        return false
    }

    fun getOpponentTeam(player: Player): MatchTeam {
        return if (this.attackTeam.contains(player)) this.defendTeam else this.attackTeam
    }

    fun getTeam(player: Player): MatchTeam {
        return if (this.attackTeam.contains(player)) this.attackTeam else this.defendTeam
    }

    fun handleVote(kitName: String) {
        val actual = this.votes[kitName]!! + 1
        this.votes[kitName] = actual
    }

    fun handleEndVote() {
        this.winKitName = this.votes.maxBy { it.value }!!.key
    }

    fun decrementCountdown(): Int {
        return --this.countdown
    }

    fun getOpponentTeam(matchTeam: MatchTeam): MatchTeam {
        return if (matchTeam == attackTeam) this.defendTeam else this.attackTeam
    }

    fun getPlayersMatch(): List<Player> {
        return arrayListOf<Player>().also {
            it.addAll(getPlayersAttack())
            it.addAll(getPlayersDefend())
        }
    }

    fun getPlayersMatchAlive(): List<TeamPlayer> {
        return arrayListOf<TeamPlayer>().also {
            it.addAll(this.attackTeam.getAliveTeamPlayers())
            it.addAll(this.defendTeam.getAliveTeamPlayers())
        }
    }

    fun getPlayersAllAlive(): List<Player> {
        return arrayListOf<Player>().also {
            it.addAll(getPlayersAttackAlive())
            it.addAll(getPlayersDefendAlive())
        }
    }

    fun getPlayersAttack(): List<Player> {
        return this.attackTeam.teamPlayers.mapNotNull { Bukkit.getPlayer(it.uuid) }
    }

    fun getPlayersAttackAlive(): List<Player> {
        return this.attackTeam.getAlivePlayers()
    }

    fun getPlayersDefendAlive(): List<Player> {
        return this.defendTeam.getAlivePlayers()
    }

    fun getPlayersDefend(): List<Player> {
        return this.defendTeam.teamPlayers.mapNotNull { Bukkit.getPlayer(it.uuid) }
    }

    fun getPlayersWorld(): List<Player> {
        return this.arena.world.players
    }

    fun getMatchTeamByPlayer(player: Player): MatchTeam {
        return if (this.getPlayersAttack().contains(player)) this.attackTeam else this.defendTeam
    }

    fun getTeamPlayerByPlayer(player: Player): TeamPlayer {
        return this.getMatchTeamByPlayer(player).getTeamPlayer(player)
    }

    fun sendTitle(title: String, subTitle: String) {
        this.getPlayersWorld().forEach {
            it.sendTitle(title, subTitle, 0, 60, 0)
        }
    }

    fun sendMessage(message: String) {
        this.getPlayersWorld().forEach { it.sendFixedMessage(message) }
    }

    fun getTeamId(player: Player): Int {
        return getTeam(player).teamId
    }

    fun sendMessage(matchTeam: MatchTeam, message: String) {
        matchTeam.sendMessage(message)
    }
}