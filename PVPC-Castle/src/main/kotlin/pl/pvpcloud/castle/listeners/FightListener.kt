package pl.pvpcloud.castle.listeners

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.profile.ProfileState

class FightListener(private val plugin: CastlePlugin) : Listener {

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
        if (this.plugin.profileManager.getProfile(event.player).isState(ProfileState.SPECTATING) && event.clickedBlock != null && event.clickedBlock.getRelative(BlockFace.UP).type == Material.FIRE) {
            event.isCancelled = true
        }
    }
}