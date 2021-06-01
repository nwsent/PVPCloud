package pl.pvpcloud.moles.team

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class TeamPlayer(
        val uuid: UUID,
        val name: String
) {
    constructor(player: Player) : this(player.uniqueId, player.name)
    constructor(uuid: UUID) : this(uuid, Bukkit.getPlayer(uuid).name)

    var alive: Boolean = true

    fun getPlayer(): Player? {
        return Bukkit.getPlayer(this.uuid)
    }
}