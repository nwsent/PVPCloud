package pl.pvpcloud.castle.queues

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class QueueEntry(
        val players: HashSet<UUID>
) {
    var queueType: QueueType = QueueType.NONE

    fun size(): Int {
        return this.players.size
    }

    fun getPlayers(): Sequence<Player> {
        return this.players.asSequence().mapNotNull { Bukkit.getPlayer(it) }
    }
}