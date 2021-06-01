package pl.pvpcloud.moles.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerFishEvent
import pl.pvpcloud.moles.MolesPlugin

class FightListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onFish(event: PlayerFishEvent) {
        if (event.state === PlayerFishEvent.State.CAUGHT_FISH) {
            event.expToDrop = 0
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity !is Player) {
            event.drops.clear()
        }
    }
}