package pl.pvpcloud.party.basic

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class PartyMember(
        val uniqueId: UUID,
        val name: String
) {

    val joinTime: Long = System.currentTimeMillis()
    var quitTime: Long = 0L

    val player: Player?
            get() = Bukkit.getPlayer(this.uniqueId)
}