package pl.pvpcloud.xvsx.arena.managers

import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.arena.arena.Arena
import pl.pvpcloud.xvsx.common.kit.Kit
import java.io.File

class ArenaManager(private val plugin: XvsXPlugin) {

    private val arenas = HashSet<Arena>()
    val arenasFolder = File(this.plugin.dataFolder, "arenas")

    init {
        if (!this.arenasFolder.exists()) {
            this.arenasFolder.mkdirs()
        }
        this.loadArenas()
    }

    fun getRandomArena(kit: Kit): Arena {
        return this.arenas
                .filter { it.enabled }
                .filter { !kit.gameSettings.excludedArenas.contains(it.name) }
                .filter { if (kit.gameSettings.arenaWhiteList.isNotEmpty()) kit.gameSettings.arenaWhiteList.contains(it.name) else true }
                .random()
    }

    fun getArena(name: String): Arena? {
        return this.arenas.singleOrNull { it.name == name }
    }

    private fun loadArenas() {
        this.plugin.configArenas.arenas.forEach {
            this.arenas.add(
                    Arena(
                            it.name,
                            it.spawnA,
                            it.spawnB,
                            it.min,
                            it.max,
                            it.enabled
                    )
            )
        }
    }

}