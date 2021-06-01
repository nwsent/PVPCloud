package pl.pvpcloud.moles.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerAchievementAwardedEvent
import org.bukkit.event.weather.ThunderChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent
import pl.pvpcloud.moles.MolesPlugin

class EnvironmentListener(private val plugin: MolesPlugin) : Listener {

    @EventHandler
    fun onSuccess(event: PlayerAchievementAwardedEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onThunderSet(event: ThunderChangeEvent) {
        if (event.toThunderState()) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onWeatherChange(event: WeatherChangeEvent) {
        if (event.toWeatherState()) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.entity !is Player) {
            event.entity.remove()
        }
        if (event.spawnReason !== CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.entity.remove()
        }
    }

    @EventHandler
    fun onBlockIgnite(event: BlockIgniteEvent) {
        if (event.cause === BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.isCancelled = true
        }
    }

    /*@EventHandler(priority = EventPriority.HIGHEST)
    fun onWorldInitEvent(event: org.bukkit.event.world.WorldInitEvent) {
        event.world.keepSpawnInMemory = false
    }*/
}