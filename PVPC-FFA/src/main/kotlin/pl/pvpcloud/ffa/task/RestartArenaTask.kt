package pl.pvpcloud.ffa.task

import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.ffa.arena.ResetBlock
import java.util.concurrent.ConcurrentHashMap

class RestartArenaTask(private val plugin: FFAPlugin) : BukkitRunnable() {

    init {
        runTaskTimer(this.plugin, 120, 120)
    }

    override fun run() {
        for (arena in this.plugin.arenaManager.arenas) {
            if (!arena.restartWorld) {
                continue
            }
            val toDelete = ConcurrentHashMap<Location, ResetBlock>()
            arena.blocks.forEach { (t: Location, u: ResetBlock) ->
                if (u.time < System.currentTimeMillis()) {
                    toDelete[t] = u
                }
            }
            toDelete.forEach { (t: Location, u: ResetBlock) ->
                t.block.type = u.mat
                t.block.data = u.data
                arena.blocks.remove(t)
            }
        }
    }

}