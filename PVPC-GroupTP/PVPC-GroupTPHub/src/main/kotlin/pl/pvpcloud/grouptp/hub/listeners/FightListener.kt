package pl.pvpcloud.grouptp.hub.listeners

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.grouptp.hub.GroupTpPlugin

class FightListener(private val plugin: GroupTpPlugin) : Listener {

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

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteractWhileDead(event: PlayerInteractEvent) {
        if (event.player.isDead) {
            event.isCancelled = true
        }
        if (event.clickedBlock != null && event.clickedBlock.getRelative(BlockFace.UP).type == Material.FIRE) {
            event.isCancelled = true
        }
    }
}