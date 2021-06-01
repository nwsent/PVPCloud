package pl.pvpcloud.tag

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TagAPI {

    internal lateinit var plugin: TagPlugin

    fun refresh() = refresh(Bukkit.getOnlinePlayers())

    fun refresh(player: Player) = plugin.tagManager.updatePlayer(player)

    fun refresh(players: Collection<Player>) = players.forEach { refresh(it) }

    fun refresh(players: ArrayList<Player>) = players.forEach { refresh(it) }
}