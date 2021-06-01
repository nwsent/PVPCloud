package pl.pvpcloud.xvsx.arena.managers

import pl.pvpcloud.xvsx.arena.XvsXPlugin
import pl.pvpcloud.xvsx.arena.match.Match
import pl.pvpcloud.xvsx.arena.match.MatchTask
import pl.pvpcloud.xvsx.arena.match.MatchTeam
import pl.pvpcloud.xvsx.arena.profile.Profile
import pl.pvpcloud.xvsx.common.packets.PacketPrepareMatch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MatchManager(private val plugin: XvsXPlugin) {

    private val matches = ConcurrentHashMap<Int, Match>()

    private val ai = AtomicInteger(0)

    fun createMatch(packet: PacketPrepareMatch) {
        val id = this.ai.getAndIncrement()
        val kit = this.plugin.commonModule.kitManager.getKit(packet.kitName)
        val arena = this.plugin.arenaManager.getRandomArena(kit)
        packet.teamA.forEach {
            val profile = Profile(it, id, 0)
            this.plugin.profileManager.profiles[it] = profile
        }
        packet.teamB.forEach {
            val profile = Profile(it, id, 1)
            this.plugin.profileManager.profiles[it] = profile
        }
        val match = Match(this.plugin, id, kit, packet.round, packet.playersSize, arena)
        this.matches[id] = match

        MatchTask(match)
    }

}