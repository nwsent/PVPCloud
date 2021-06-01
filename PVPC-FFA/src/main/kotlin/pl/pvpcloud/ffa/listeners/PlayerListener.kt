package pl.pvpcloud.ffa.listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.ranking.RankingAlgorithm
import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.fight.enums.FightKillerType
import pl.pvpcloud.fight.event.PlayerFightDeathEvent
import pl.pvpcloud.guild.api.GuildsAPI
import pl.pvpcloud.guild.api.packet.PacketGuildUpdate
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.statistics.StatsAPI
import pl.pvpcloud.tools.ToolsAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class PlayerListener(private val plugin: FFAPlugin) : Listener {

    private val map = ConcurrentHashMap<UUID, Long>()
    
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            this.plugin.arenaManager.handlePlayerJoin(player)
        }, 5)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        this.plugin.arenaManager.handlePlayerQuit(player)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val arena = this.plugin.arenaManager.getArenaByPlayer(player)
        event.respawnLocation = arena.spawnLocation
        Bukkit.getScheduler().runTaskLater(this.plugin, {
            if (Bukkit.getPlayer(player.uniqueId) != null) {
                arena.handleDeath(player)
            }
        }, 3)
    }

    @EventHandler
    fun onWaterEmpty(event: PlayerBucketEmptyEvent) {
        val block = event.blockClicked.getRelative(event.blockFace, 1)
        if (block.type.toString().contains("WATER")) {
            event.isCancelled = true
            return
        }
        val player = event.player
        val mat: Material = block.type
        val data: Byte = block.data
        val slot: Int = player.inventory.heldItemSlot
        if (event.bucket === Material.WATER_BUCKET) {
            Bukkit.getScheduler().runTaskLater(this.plugin, {
                block.type = mat
                block.data = data
                player.inventory.removeItem(ItemStack(Material.WATER_BUCKET, 16))
                player.inventory.removeItem(ItemStack(Material.BUCKET, 16))
                player.inventory.setItem(slot, ItemStack(Material.WATER_BUCKET))
                event.isCancelled = true
            }, 40L)
        }
    }

    @EventHandler
    fun newApple(event: PlayerItemConsumeEvent) {
        val itemStack: ItemStack = event.item
        if (itemStack.type != Material.GOLDEN_APPLE) {
            return
        }
        val p: Player = event.player
        val arena = this.plugin.arenaManager.getArenaByPlayer(p)
        if (itemStack.durability.toInt() == 1) {
            val effects = this.plugin.config.effectsKox[arena.kitName]
                    ?: return
            Bukkit.getScheduler().runTask(this.plugin) {
                effects.forEach { it.removePlayer(p) }
            }
            Bukkit.getScheduler().runTask(this.plugin) {
                effects.forEach { it.givePlayer(p) }
            }
        } else {
            val effects = this.plugin.config.effectsRef[arena.kitName]
                    ?: return
            Bukkit.getScheduler().runTask(this.plugin) {
                effects.forEach { it.removePlayer(p) }
            }
            Bukkit.getScheduler().runTask(this.plugin) {
                effects.forEach { it.givePlayer(p) }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerFishingEvent(event: PlayerFishEvent) {
        val player = event.player
        val state = event.state

        if (state != PlayerFishEvent.State.IN_GROUND && state != PlayerFishEvent.State.FAILED_ATTEMPT) return

        val nextMessageTime = map[player.uniqueId] ?: 0L + 30000L
        if (nextMessageTime > System.currentTimeMillis()) {
            return player.sendFixedMessage("&4Ups! &fPoczekaj &c&l${TimeUnit.MILLISECONDS.toSeconds(nextMessageTime - System.currentTimeMillis())} &faby uzyć kolejny raz.")
        }
        map[player.uniqueId] = System.currentTimeMillis() + 30000L

        val bh = 9.0 / 17.5
        val bw = 10.0 / 3.4

        player.velocity = player.location.direction.setY(bh).multiply(bw)
    }


}