package pl.pvpcloud.standard.arena

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.standard.StandardPlugin
import pl.pvpcloud.standard.kit.KitType
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

data class Arena(
        val plugin: StandardPlugin,
        val players: ArrayList<UUID>,
        val kitType: KitType
) {

    init {
        ArenaTask(this)
    }

    //Musi byc jedna sekunda bo nulla wywala :[
    var timeArena: Long = TimeUnit.SECONDS.toMillis(1)
    var timeToStart: Int = 4
    var timeToEnd: Int = 6

    val arenaUUID: UUID = UUID.randomUUID()

    var arenaState: ArenaState = ArenaState.WAITING

    fun getPlayers() =
         this.players.mapNotNull { Bukkit.getPlayer(it) }

}