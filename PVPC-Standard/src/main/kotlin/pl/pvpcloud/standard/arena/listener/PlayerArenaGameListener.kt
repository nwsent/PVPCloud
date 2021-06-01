package pl.pvpcloud.standard.arena.listener

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.standard.arena.ArenaState
import pl.pvpcloud.standard.user.UserState
import pl.pvpcloud.tools.ToolsAPI
import java.text.DecimalFormat
import kotlin.math.roundToInt


class PlayerArenaGameListener(private val plugin: StandardPlugin) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        val user = this.plugin.userManager.findUser(player.uniqueId)
        val arena = this.plugin.arenaManager.getArena(user.arenaUUID!!)

        arena.arenaState = ArenaState.ENDING
        arena.players.remove(player.uniqueId)
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as Player

        val user = this.plugin.userManager.findUser(player.uniqueId)
        if (user.userState == UserState.FIGHT) {
            val arena = this.plugin.arenaManager.getArena(user.arenaUUID!!)
            arena.arenaState = ArenaState.ENDING
            arena.players.remove(player.uniqueId)

            user.arenaUUID = null
            user.userState = UserState.WAIT
        }

        val killer = event.entity.killer as Player
        ToolsAPI.addCoins(killer.uniqueId, 1)

        val heart = killer.health / 2
        val decimal = DecimalFormat("#.#")

        Bukkit.getOnlinePlayers()
                .forEach {
                    it.sendFixedMessage(" &f${killer.name} &ewygral arene z: &f${player.name} &ena &f${decimal.format(heart)}&c\u2764")
                }
        event.deathMessage = null
        event.drops.clear()

        player.spigot().respawn()
        this.plugin.arenaManager.getSpawn(player)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onKick(event: PlayerKickEvent) {
        val player = event.player

        val user = this.plugin.userManager.findUser(player.uniqueId)
        val arena = this.plugin.arenaManager.getArena(user.arenaUUID!!)

        arena.arenaState = ArenaState.ENDING
        arena.players.remove(player.uniqueId)
    }

}