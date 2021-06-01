package pl.pvpcloud.castle.match

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.castle.queues.QueueEntry
import pl.pvpcloud.common.extensions.sendFixedMessage
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

class MatchTeam(
        val teamId: Int
) {
    val players: HashSet<UUID> = LinkedHashSet()

    fun add(queueEntry: QueueEntry) {
        queueEntry.players.forEach {
            this.players.add(it)
        }
    }

    fun contains(player: Player): Boolean {
        return this.players.contains(player.uniqueId)
    }

    fun getAlivePlayers(): Sequence<Player> {
        return this.players.asSequence().mapNotNull { Bukkit.getPlayer(it) }.filter { it.gameMode === GameMode.SURVIVAL }
    }

    fun getAlivePlayersAll(): Sequence<Player> {
        return this.players.asSequence().mapNotNull { Bukkit.getPlayer(it) }
    }

    fun getAlivePlayersCount(): Int {
        return this.getAlivePlayers().count()
    }

    fun sendMessage(message: String) {
        this.getAlivePlayers().forEach { it.sendFixedMessage(message) }
    }
}