package pl.pvpcloud.moles.match

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.queues.QueueEntry
import pl.pvpcloud.moles.team.TeamPlayer
import java.util.*

class MatchTeam(
        val teamId: Int
) {
    val teamPlayers: MutableSet<TeamPlayer> = Collections.synchronizedSet(hashSetOf<TeamPlayer>())

    fun add(queueEntry: QueueEntry) {
        queueEntry.players.forEach {
            TeamPlayer(it).also { p ->
                this.teamPlayers.add(p)
            }
        }
    }

    fun contains(player: Player): Boolean {
        return this.teamPlayers.any { it.uuid === player.uniqueId }
    }

    fun getTeamPlayer(player: Player): TeamPlayer {
        return this.teamPlayers.single { it.uuid === player.uniqueId }
    }

    fun getAliveTeamPlayers(): List<TeamPlayer> {
        return this.teamPlayers.filter { it.alive }
    }

    fun getAlivePlayers(): List<Player> {
        return this.getAliveTeamPlayers().mapNotNull { it.getPlayer() }
    }

    fun killPlayer(player: Player) {
        this.teamPlayers.single { it.uuid == player.uniqueId }.alive = false
    }

    fun getAliveCount(): Int {
        return this.teamPlayers.count { it.alive }
    }

    fun size(): Int {
        return this.teamPlayers.size
    }

    fun sendMessage(message: String) {
        this.getAlivePlayers().forEach { it.sendFixedMessage(message) }
    }
}