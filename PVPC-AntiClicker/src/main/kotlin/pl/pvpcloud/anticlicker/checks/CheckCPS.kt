package pl.pvpcloud.anticlicker.checks

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pl.pvpcloud.anticlicker.AntiClickerPlugin
import pl.pvpcloud.anticlicker.basic.Profile
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CheckCPS(private val plugin: AntiClickerPlugin): Listener {

    private val profiles = ConcurrentHashMap<UUID, Profile>()

    fun getProfileOrCreate(uniqueId: UUID) : Profile {
        if (this.profiles.containsKey(uniqueId)) {
            return this.profiles[uniqueId]!!
        }
        val profile = Profile(uniqueId, arrayListOf(), System.currentTimeMillis() - 500L)
        this.profiles[uniqueId] = profile
        return profile
    }

    init {
        this.plugin.protocolManager.addPacketListener(object : PacketAdapter(this.plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.USE_ENTITY) {
            override fun onPacketReceiving(event: PacketEvent) {
                if (event.packetType === PacketType.Play.Client.USE_ENTITY) {
                    if (event.packet.entityUseActions.read(0) == EnumWrappers.EntityUseAction.ATTACK) {
                        val player = event.player
                        val profile = getProfileOrCreate(player.uniqueId)
                        val lastHitTime = System.currentTimeMillis() - profile.lastHit
                        if (lastHitTime <= 2) {
                            player.sendFixedMessage("&ePusty HIT! $lastHitTime")
                            event.isCancelled = true
                            return
                        }
                        profile.lastHit = System.currentTimeMillis()
                        profile.lastHits.add(lastHitTime)
                        if (profile.lastHits.size >= 15) {
                            val avg = profile.lastHits.average()
                            if (avg < 65) {
                                disableTime[player.uniqueId] = System.currentTimeMillis() + 3000
                                player.sendTitle("", this@CheckCPS.plugin.config.blockTitle.rep("%cps%", this@CheckCPS.plugin.config.maxCps.toString()), 0, 40, 0)
                                player.sendFixedMessage(this@CheckCPS.plugin.config.alertMessage.rep("%cps%", this@CheckCPS.plugin.config.maxCps.toString()))
                                player.addPotionEffects(potions)
                                player.sendFixedMessage("&eAVG: $avg")
                            }
                            profile.lastHits.clear()
                        }
                    }
                }
            }
        })
    }

    private val disableTime = ConcurrentHashMap<UUID, Long>()

    private val potions = arrayListOf(PotionEffect(PotionEffectType.BLINDNESS, 80, 0), PotionEffect(PotionEffectType.SLOW, 80, 1))

    private fun isDisabled(player: Player): Boolean {
        if (!disableTime.containsKey(player.uniqueId)) {
            disableTime[player.uniqueId] = System.currentTimeMillis()
        }
        return disableTime[player.uniqueId]!! > System.currentTimeMillis()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.damager.type != EntityType.PLAYER) {
            return
        }
        if (event.entity.type != EntityType.PLAYER) {
            return
        }
        if (event.cause == EntityDamageEvent.DamageCause.FALL) {
            return
        }
        val player = event.damager as Player
        if (isDisabled(player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        profiles.remove(event.player.uniqueId)
        disableTime.remove(event.player.uniqueId)
    }

}