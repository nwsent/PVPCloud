package pl.pvpcloud.castle.managers

import org.apache.commons.io.FileUtils
import org.bukkit.*
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.arena.Arena
import pl.pvpcloud.castle.arena.ArenaProfile
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class ArenaManager(private val plugin: CastlePlugin) {

    private val arenasFolder = File(this.plugin.dataFolder, "arenas")
    val arenasProfile = ConcurrentHashMap.newKeySet<ArenaProfile>()
    val arenas = ConcurrentHashMap<Int, Arena>()

    private val ai = AtomicInteger(0)

    val executor = Executors.newSingleThreadExecutor()

    init {
        if (!this.arenasFolder.exists()) {
            this.arenasFolder.mkdirs()
        }
        this.loadArenaProfiles()
    }


    fun loadArenaProfiles() {
        this.arenasProfile.clear()
        this.plugin.configArenas.arenas.forEach {
            this.arenasProfile.add(it)
        }
    }

    fun getFreeArena(arenaProfileName: String): Arena {
        val arenaProfile = this.arenasProfile.single { it.name == arenaProfileName }
        val id = this.ai.getAndIncrement()
        val worldName = "${arenaProfile.name}-$id"
        this.copyWorldIntoServer(arenaProfile.name, worldName)
        val arena = Arena(id, arenaProfile, worldName)
        this.arenas[id] = arena
        this.load(arena)
        return arena
    }

    fun getArenaByWorld(worldName: String): Arena? {
        return this.arenas.values.firstOrNull { it.worldName == worldName }
    }

    private fun copyWorldIntoServer(worldName: String, newName: String) {
        val worldFile = File(this.arenasFolder, worldName)

        val destFile = File(this.plugin.server.worldContainer, newName)

        FileUtils.copyDirectory(worldFile, destFile)
    }

    fun load(arena: Arena) {
        Runnable {
            val creator = WorldCreator(arena.worldName)
                    .environment(World.Environment.NORMAL)
                    .generateStructures(false)
                    .type(WorldType.NORMAL)
            val world = creator.createWorld()
            world.isAutoSave = false
            world.setSpawnFlags(false, false)
            world.difficulty = Difficulty.EASY
            world.pvp = true
            arena.world = world
            world.worldBorder.center = arena.getDefendSpawn()
            world.worldBorder.size = arena.worldBorderSize
        }.also {
            this.executor.execute(it)
        }
    }

    fun unload(arena: Arena) {
        this.arenas.remove(arena.id)
        Bukkit.getServer().unloadWorld(arena.world, false)
        val worldFile = arena.world.worldFolder
        FileUtils.deleteDirectory(worldFile)
        println("\n ${arena.worldName} was DELETED")
    }

}