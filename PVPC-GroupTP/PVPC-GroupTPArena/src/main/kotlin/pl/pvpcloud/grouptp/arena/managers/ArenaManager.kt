package pl.pvpcloud.grouptp.arena.managers

import org.apache.commons.io.FileUtils
import org.bukkit.*
import pl.pvpcloud.grouptp.arena.GroupTpPlugin
import pl.pvpcloud.grouptp.arena.arena.Arena
import pl.pvpcloud.grouptp.arena.arena.ArenaProfile
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class ArenaManager(private val plugin: GroupTpPlugin) {

    private val arenasFolder = File(this.plugin.dataFolder, "arenas")
    private val arenasProfile = ConcurrentHashMap.newKeySet<ArenaProfile>()
    val arenas = ConcurrentHashMap<Int, Arena>()

    init {
        if (!this.arenasFolder.exists()) {
            this.arenasFolder.mkdirs()
        }
        this.plugin.configArenas.arenas.forEach {
            this.arenasProfile.add(it)
        }
    }

    fun createNewArena(id: Int): Arena {
        val arenasProfileShuffled = this.arenasProfile.toList().shuffled()
        val arenaProfile = arenasProfileShuffled[0]
        val worldName = "${arenaProfile.name}-$id"
        this.copyWorldIntoServer(arenaProfile.name, worldName)
        val arena = Arena(id, arenaProfile, worldName)
        this.arenas[id] = arena
        this.load(arena)
        return arena
    }

    private fun copyWorldIntoServer(worldName: String, newName: String) {
        val worldFile = File(this.arenasFolder, worldName)

        val destFile = File(this.plugin.server.worldContainer, newName)

        FileUtils.copyDirectory(worldFile, destFile)
    }

    fun load(arena: Arena) {
        val creator = WorldCreator(arena.worldName)
                .environment(World.Environment.NORMAL)
                .generateStructures(false)
                .type(WorldType.FLAT)
        val world = creator.createWorld()
        world.isAutoSave = false
        world.setSpawnFlags(false, false)
        world.difficulty = Difficulty.EASY
        world.pvp = true
        arena.world = world
        world.worldBorder.center = arena.getSpawn()
        world.worldBorder.size = arena.worldBorderSize
    }

    fun unload(arena: Arena) {
        this.arenas.remove(arena.id)
        Bukkit.getServer().unloadWorld(arena.world, false)
        val worldFile = arena.world.worldFolder
        FileUtils.deleteDirectory(worldFile)
        println("\n ${arena.worldName} was DELETED")
    }
}