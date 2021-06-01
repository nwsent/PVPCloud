package pl.pvpcloud.standard.arena

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.standard.helper.BorderHelper
import java.util.*

class ArenaManager(private val plugin: StandardPlugin) {

    private val arenas = hashMapOf<UUID, Arena>()

    fun addArena(arena: Arena) = this.arenas.put(arena.arenaUUID, arena)

    fun removeArena(uuid: UUID) = this.arenas.remove(uuid)

    fun getArena(uuid: UUID) = this.arenas[uuid]
            ?: throw NullPointerException("arena is null $uuid")

    fun getArenas() = this.arenas

    fun getSpawnLocation() = Location(Bukkit.getWorld("world"), 0.0, 68.0, 0.0)

    fun getSpawn(player: Player) {
        val user = this.plugin.userManager.findUser(player.uniqueId)

        this.plugin.server.scheduler.runTaskLater(this.plugin, {

            player.gameMode = GameMode.ADVENTURE
            player.teleport(this.getSpawnLocation())

            BorderHelper.setBorder(player, this.getSpawnLocation(), 100)
            this.plugin.kitManager.giveKit(player, user.kitType, true)
            Bukkit.getOnlinePlayers().forEach { all -> player.showPlayer(all) }

        }, 2)

    }

}