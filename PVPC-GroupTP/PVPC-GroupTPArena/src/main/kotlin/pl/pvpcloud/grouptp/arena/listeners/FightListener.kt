package pl.pvpcloud.grouptp.arena.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerFishEvent
import pl.pvpcloud.grouptp.arena.GroupTpPlugin

class FightListener(private val plugin: GroupTpPlugin) : Listener {

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity !is Player) {
            event.drops.clear()
        }
    }
}