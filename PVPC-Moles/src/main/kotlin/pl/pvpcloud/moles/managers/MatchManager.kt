package pl.pvpcloud.moles.managers

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.arena.Arena
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.moles.match.event.MatchStartEvent
import pl.pvpcloud.moles.profile.Profile
import pl.pvpcloud.moles.profile.ProfileState
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MatchManager(private val plugin: MolesPlugin) {

    val matches = ConcurrentHashMap<UUID, Match>()

    fun getMatch(profile: Profile): Match? {
        return this.matches[profile.matchId]
    }

    fun getMatch(uuidPlayer: UUID): Match? {
        return this.matches[this.plugin.profileManager.getProfile(uuidPlayer).matchId]
    }

    fun getMatchByUUID(uuid: UUID): Match? {
        return this.matches[uuid]
    }

    fun getMatchByArena(arena: Arena): Match? {
        return this.matches.values.firstOrNull { it.arena == arena }
    }

    fun createMatch(match: Match) {
        this.matches[match.matchId] = match
        Bukkit.getScheduler().runTask(this.plugin) {
            this.plugin.server.pluginManager.callEvent(MatchStartEvent(match))
        }
    }

    fun deleteMatch(match: Match) {
        this.matches.remove(match.matchId)
    }

    fun getTag(sender: Player, receiver: Player): String {

        val sp = this.plugin.profileManager.getProfile(sender)
        val sr = this.plugin.profileManager.getProfile(receiver)
        if (sr.isState(ProfileState.FIGHTING) || sp.isState(ProfileState.FIGHTING)) {
            return if (sp.teamId == sr.teamId) "&a" else "&c"
        }
        return ""
    }
}