package pl.pvpcloud.ffa.manager

import com.google.common.cache.CacheBuilder
import org.bukkit.entity.Player
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.ffa.arena.Arena
import pl.pvpcloud.ffa.arena.impl.Server
import pl.pvpcloud.packets.hub.PacketTransfer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ArenaManager(private val plugin: FFAPlugin) {

    val arenas = hashSetOf<Arena>()
    private val playersArena = ConcurrentHashMap<UUID, String>()

    private val transfers = CacheBuilder.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build<UUID, String>()

    init {
        this.arenas.addAll(
            arrayListOf(
                Server()
            )
        )

        this.arenas.forEach {
            this.plugin.registerListeners(it)
        }
    }

    fun getArena(name: String): Arena {
        return this.arenas.single { it.name == name }
    }

    fun getArenaByPlayer(player: Player): Arena {
        return this.getArena(this.playersArena[player.uniqueId]!!)
    }

    fun getPlayerByArena(name: String): Set<UUID> {
        return this.playersArena.filter { it.value == name }.keys
    }

    fun handlePlayerJoin(player: Player) {
        val arenaName = this.transfers.getIfPresent(player.uniqueId)
        if (arenaName == null) {
            if (player.hasPermission("tools.command.teleport")) {
                return
            }
            player.kickPlayer("Wejdz jeszcze raz!")
            return
        }
        this.playersArena[player.uniqueId] = arenaName

        val arena = this.getArena(arenaName)
        arena.handleJoin(player)
    }

    fun handlePlayerQuit(player: Player) {
        this.playersArena.remove(player.uniqueId)
    }

    fun handleTransferPacket(packetTransfer: PacketTransfer) {
        this.transfers.put(packetTransfer.uniqueId, packetTransfer.arenaName)
    }

}