package pl.pvpcloud.xvsx.hub.managers

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.hotbar.HotBarLayout
import pl.pvpcloud.xvsx.hub.profile.Profile
import pl.pvpcloud.xvsx.hub.profile.ProfileState
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ProfileManager(private val plugin: XvsXPlugin) : Listener {

    private val profiles = ConcurrentHashMap<UUID, Profile>()

    fun getProfile(uniqueId: UUID): Profile? =
            this.profiles[uniqueId]

    fun findProfile(uniqueId: UUID): Profile =
            this.profiles[uniqueId] ?:
                    throw NullPointerException("Profile is null")

    fun findProfile(player: Player): Profile =
            this.profiles[player.uniqueId] ?:
            throw NullPointerException("Profile is null")

    fun createProfile(player: Player) =
            Profile(player.uniqueId, player.name).also {
                this.profiles[player.uniqueId] = it
            }

    fun apply(player: Player, profileState: ProfileState) {
        this.apply(player, profileState, false)
    }

    fun apply(player: Player, profileState: ProfileState, teleport: Boolean) {
        this.apply(player, profileState, teleport, false)
    }

    fun apply(player: Player, profileState: ProfileState, teleport: Boolean, connect: Boolean) {
        val profile = findProfile(player)
        profile.profileState = profileState
        if (connect) {
            player.healthScale = 20.0
            player.maxHealth = 20.0
            player.health = 20.0
            player.spigot().collidesWithEntities = false
            player.canPickupItems = false
            player.inventory.heldItemSlot = 0
            player.level = 0
            player.foodLevel = 20
            player.saturation = 12.8f
            player.totalExperience = 0
            player.exp = 0.0f
            player.isFlying = false
            player.allowFlight = false
            player.fireTicks = 0
            player.maximumNoDamageTicks = 20
            player.isSneaking = false
            player.isSprinting = false
            player.walkSpeed = 0.2f
            player.itemOnCursor = null
            player.fallDistance = 0.0f
            player.gameMode = GameMode.ADVENTURE
        }
        when (profile.profileState) {
            ProfileState.LOBBY -> {
                player.healthScale = 20.0
                player.maxHealth = 20.0
                player.health = 20.0
                player.spigot().collidesWithEntities = false
                player.canPickupItems = false
                player.inventory.heldItemSlot = 0
                player.isFlying = false
                player.foodLevel = 20
                player.saturation = 12.8f
                player.allowFlight = false
                player.fireTicks = 0
                player.maximumNoDamageTicks = 20
                player.isSneaking = false
                player.isSprinting = false
                player.walkSpeed = 0.2f
                player.itemOnCursor = null
                player.fallDistance = 0.0f
                player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
                player.gameMode = GameMode.ADVENTURE
            }
        }
        if (teleport) {
            player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
        }
        this.refreshHotBar(player)
    }

    fun refreshHotBar(player: Player) {
        player.clearInventory()
        val profile = this.findProfile(player)
        when (profile.profileState) {
            ProfileState.LOBBY -> player.inventory.contents = this.plugin.hotBarManager.getLayout(HotBarLayout.LOBBY, player)
            ProfileState.QUEUING -> player.inventory.contents = this.plugin.hotBarManager.getLayout(HotBarLayout.QUEUE, player)
        }
        player.updateInventory()
    }

}