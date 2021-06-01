package pl.pvpcloud.moles.hub.queues

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class QueueEntry(
        val players: HashSet<UUID>
) {

    fun size(): Int {
        return this.players.size
    }

    fun isParty(): Boolean {
        return this.players.size > 1
    }

    fun getPlayers(): Sequence<Player> {
        return this.players.asSequence().mapNotNull { Bukkit.getPlayer(it) }
    }

    fun first(): UUID {
        return this.players.first()
    }
}