package pl.pvpcloud.moles.managers

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.common.packets.PacketPrepareMatch
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.moles.match.MatchTask
import pl.pvpcloud.moles.match.MatchTeam
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MatchManager(private val plugin: MolesPlugin) {

    private val matches = ConcurrentHashMap<Int, Match>()

    private val playerMatches = ConcurrentHashMap<UUID, Int>()

    val playerMatchTeamId = ConcurrentHashMap<UUID, Int>()

    private val ai = AtomicInteger(0)

    fun getMatch(player: Player): Match? {
        return this.matches[this.playerMatches[player.uniqueId]]
    }

    fun getMatchByWorld(uniqueIdWorld: UUID): Match? {
        return this.matches.values.singleOrNull { it.arena.world.uid == uniqueIdWorld }
    }

    fun createMatch(packet: PacketPrepareMatch) {
        val matchId = ai.getAndIncrement()
        val arena = this.plugin.arenaManager.createNewArena(matchId)
        val teamA = MatchTeam(0, packet.teamA.toHashSet())
        val teamB = MatchTeam(1, packet.teamB.toHashSet())
        val match = Match(this.plugin, matchId, arena, teamA, teamB)
        this.matches[match.matchId] = match
        packet.teamA.forEach {
            this.playerMatchTeamId[it] = 0
            this.playerMatches[it] = matchId
        }
        packet.teamB.forEach {
            this.playerMatchTeamId[it] = 1
            this.playerMatches[it] = matchId
        }
        MatchTask(match)
    }

    fun sendToHubMoles(player: Player) {
        val connectPlayer = ConnectAPI.getPlayerByUUID(player.uniqueId)
                ?: return
        player.sendTitle("", "&cNie wszyscy dolaczyli do areny", 0, 40, 0)
        connectPlayer.connect("moles_hub")
    }

    fun deleteMatch(match: Match) {
        this.matches.remove(match.matchId)
    }

    fun getTag(sender: Player, receiver: Player): String {
        val sp = this.playerMatchTeamId[sender.uniqueId]
        val sr = this.playerMatchTeamId[receiver.uniqueId]
        return if (sp == sr) "&a" else "&c"
    }
}