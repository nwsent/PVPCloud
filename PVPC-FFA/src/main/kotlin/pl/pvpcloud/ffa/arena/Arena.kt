package pl.pvpcloud.ffa.arena

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.concurrent.ConcurrentHashMap

abstract class Arena(
        val name: String,
        val kitName: String,
        val restartWorld: Boolean,
        val saveInventory: Boolean
) : Listener {
        val blocks = ConcurrentHashMap<Location, ResetBlock>()

        lateinit var spawnLocation: Location
        lateinit var cuboidLocation: Location

        abstract fun handleJoin(player: Player)

        abstract fun handleDeath(player: Player)
}