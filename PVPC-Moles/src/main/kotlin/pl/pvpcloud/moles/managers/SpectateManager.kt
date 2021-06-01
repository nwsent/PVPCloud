package pl.pvpcloud.moles.managers

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.moles.profile.ProfileState
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class SpectateManager(private val plugin: MolesPlugin) {

    private val spectators = ConcurrentHashMap<UUID, Match>()

    fun addSpectate(player: Player, match: Match, deathSpectator: Boolean) {
        this.spectators[player.uniqueId] = match
        if (deathSpectator) {
            player.clearInventory()
            player.health = 20.0
            player.foodLevel = 20
            player.saturation = 20.0f
            player.exhaustion = 0.0f
            player.fireTicks = 0
            player.activePotionEffects.forEach {
                player.removePotionEffect(it.type)
            }
        }
        this.plugin.profileManager.apply(player, ProfileState.SPECTATING)
    }

    fun removeSpectate(player: Player) {
        this.plugin.profileManager.apply(player, ProfileState.LOBBY, true)
        this.spectators.remove(player.uniqueId)
    }

    fun removeSpectators(match: Match) {
        this.spectators
                .filter { it.value.matchId == match.matchId }
                .mapNotNull { Bukkit.getPlayer(it.key) }
                .forEach { this.removeSpectate(it) }
    }

    fun getMatch(uuid: UUID): Match? {
        return this.spectators[uuid]
    }
}
