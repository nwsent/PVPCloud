package pl.pvpcloud.tools.listeners

import com.google.common.cache.CacheBuilder
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.giveOrDropItem
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import java.util.*
import java.util.concurrent.TimeUnit

class EnderPearlListener(private val plugin: ToolsPlugin) : Listener {

    private val coolDowns = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build<UUID, Long>()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onHit1(e: ProjectileHitEvent) {
        val pr: Projectile = e.entity
        if (pr.type != EntityType.ENDER_PEARL)
            return

        if (pr.shooter is Player) {
            val player: Player = pr.shooter as Player
            val bxn: Block = player.location.add(-1.0, 0.0, 0.0).block
            val bxn1: Block = player.location.add(-0.5, 0.0, 0.0).block
            val bxp: Block = player.location.add(1.0, 0.0, 0.0).block
            val bxp1: Block = player.location.add(0.5, 0.0, 0.0).block
            val bzn: Block = player.location.add(0.0, 0.0, -1.0).block
            val bzn1: Block = player.location.add(0.0, 0.0, -0.5).block
            val bzp: Block = player.location.add(0.0, 0.0, 1.0).block
            val bzp1: Block = player.location.add(0.0, 0.0, 0.5).block
            val by: Block = player.location.block
            if (by.type != Material.AIR) {
                player.teleport(player.location.add(0.0, 0.5, 0.0))
            }
            if (bxn.type != Material.AIR || bxn1.type != Material.AIR) {
                player.teleport(player.location.add(0.5, 0.0, 0.0))
            }
            if (bxp.type != Material.AIR || bxp1.type != Material.AIR) {
                player.teleport(player.location.add(-0.5, 0.0, 0.0))
            }
            if (bzn.type != Material.AIR || bzn1.type != Material.AIR) {
                player.teleport(player.location.add(0.0, 0.0, 0.5))
            }
            if (bzp.type == Material.AIR && bzp1.type == Material.AIR) {
                return
            }
            player.teleport(player.location.add(0.0, 0.0, -0.5))
        }
    }

    @EventHandler
    fun onLunch(event: ProjectileLaunchEvent) {
        val pro: Projectile = event.entity
        if (pro.type != EntityType.ENDER_PEARL) return

        if (NetworkAPI.id != "lobby") {
            val player = pro.shooter as Player
            val lastLunch = this.coolDowns.getIfPresent(player.uniqueId)
            if (lastLunch != null && lastLunch >= System.currentTimeMillis()) {
                player.sendFixedMessage("&7» &fNastępnej perły będziesz mógł użyć za&8: &e${TimeUnit.MILLISECONDS.toSeconds(lastLunch - System.currentTimeMillis())}&csek.")
                event.isCancelled = true
                player.giveOrDropItem(ItemStack(Material.ENDER_PEARL, 1))
                return
            }
            this.coolDowns.put(player.uniqueId, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(2))
        }
    }

}