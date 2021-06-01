package pl.pvpcloud.moles.queues

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class QueueEntry(
        val players: HashSet<UUID>
) {

    fun size(): Int {
        return this.players.size
    }

    fun getPlayers(): List<Player> {
        return this.players.mapNotNull { Bukkit.getPlayer(it) }
    }
}