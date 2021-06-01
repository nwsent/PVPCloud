package pl.pvpcloud.xvsx.hub.queues

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Stream

class QueueEntry(
        val players: HashSet<UUID>
) {

    fun size(): Int =
            this.players.size

    fun getPlayers(): Stream<Player> =
            this.players.stream().map { Bukkit.getPlayer(it) }

}