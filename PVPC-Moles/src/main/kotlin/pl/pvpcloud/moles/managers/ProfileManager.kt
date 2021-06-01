package pl.pvpcloud.moles.managers

import org.bukkit.GameMode
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.hotbar.HotbarLayout
import pl.pvpcloud.moles.profile.Profile
import pl.pvpcloud.moles.profile.ProfileState
import pl.pvpcloud.moles.profile.ProfileState.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ProfileManager(private val plugin: MolesPlugin) {

    private val profiles = ConcurrentHashMap<UUID, Profile>()

    fun getProfile(uuid: UUID): Profile = this.profiles[uuid]
            ?: throw NullPointerException("profile is null $uuid")

    fun getProfile(player: Player): Profile = this.profiles[player.uniqueId]
            ?: throw NullPointerException("profile is null ${player.uniqueId}")

    fun getProfileOrNull(player: Player): Profile? = this.profiles[player.uniqueId]

    fun createProfile(player: Player): Profile {
        val profile = Profile(player.uniqueId, player.name)
        this.profiles[profile.uuid] = profile
        return profile
    }

    fun isBusy(player: Player): Boolean {
        return !getProfile(player.uniqueId).isState(LOBBY)
    }

    fun apply(player: Player, profileState: ProfileState) {
        this.apply(player, profileState, false)
    }

    fun apply(player: Player, profileState: ProfileState, teleport: Boolean) {
        this.apply(player, profileState, teleport, false)
    }

    fun apply(player: Player, profileState: ProfileState, teleport: Boolean, connect: Boolean) {
        val profile = getProfile(player)
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
            LOBBY -> {
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
                profile.teamId = -1
                profile.matchId = null
            }
            SPECTATING -> {
                player.allowFlight = true
                player.isFlying = true
                player.flySpeed = 0.2F
                player.spigot().collidesWithEntities = false
                player.itemOnCursor = null
                player.gameMode = GameMode.SPECTATOR
                profile.teamId = -1
            }
        }
        if (teleport) {
            player.teleport(this.plugin.config.spawnLocation.toBukkitLocation())
        }
        this.refreshHotbar(player)
    }

    fun refreshHotbar(player: Player) {
        player.clearInventory()
        val profile = this.getProfile(player)
        when (profile.profileState) {
            LOBBY -> player.inventory.contents = this.plugin.hotbarManager.getLayout(HotbarLayout.LOBBY, player)
            SPECTATING -> player.inventory.contents = this.plugin.hotbarManager.getLayout(HotbarLayout.MATCH_SPECTATE, player)
            QUEUING -> player.inventory.contents = this.plugin.hotbarManager.getLayout(HotbarLayout.QUEUE, player)
        }
        player.updateInventory()
    }
}