package pl.pvpcloud.xvsx.hub.managers

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.party.PartyAPI
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.profile.ProfileState
import pl.pvpcloud.xvsx.hub.queues.QueueEntry
import pl.pvpcloud.xvsx.hub.queues.QueueSize
import pl.pvpcloud.xvsx.hub.queues.QueueThread
import pl.pvpcloud.xvsx.hub.queues.QueueWait
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class QueueManager(private val plugin: XvsXPlugin) {

    private val ai = AtomicInteger(0)
    val queuesWait = ConcurrentHashMap<Int, QueueWait>()

    val queueThread = QueueThread(this.plugin)

    //todo poprawić że ja będzie 3vs3 to jak bedzie 3x2osoby z party to sie wyjebie bo nie przydzieli

    fun addToQueue(player: Player, kitName: String, queueSize: QueueSize) {
        val profile = this.plugin.profileManager.findProfile(player)
        if (profile.profileState != ProfileState.LOBBY) {
            return player.sendFixedMessage("&cJestes w kolejce")
        }
        val party = PartyAPI.getParty(player.uniqueId)
        val queueEntry = if (party != null) {
            if (party.uniqueIdLeader != player.uniqueId) {
                player.sendFixedMessage("&cNie jesteś liderem")
                return
            }
            if (party.members.size > (queueSize.size/2)) {
                player.sendFixedMessage("&cPosiadasz zbyt wiele osób w party")
                return
            }
            if (party.membersOnline().none { this.plugin.profileManager.findProfile(it).profileState == ProfileState.LOBBY }) {
                player.sendFixedMessage("&cNie wszyscy gracze sa w lobby")
                return
            }
            QueueEntry(party.membersOnline().map { it.uniqueId }.toHashSet())
        } else {
            QueueEntry(hashSetOf(player.uniqueId))
        }
        val queueWait = this.getFreeQueueWait(queueEntry, kitName, queueSize)
        queueWait.queueEntries.add(queueEntry)
        queueEntry.getPlayers().forEach {
            this.plugin.profileManager.apply(it, ProfileState.QUEUING)
            it.sendFixedMessage("&8* &eDołączyłeś do kolejki")
        }
    }

    fun leaveFromQueue(player: Player) {
        val queueWait = this.getQueueWait(player.uniqueId)
        if (queueWait != null) {
            val queueEntry = queueWait.queueEntries.single { it.players.contains(player.uniqueId) }
            queueWait.queueEntries.remove(queueEntry)
            queueEntry.getPlayers().forEach {
                this.plugin.profileManager.apply(it, ProfileState.LOBBY)
                player.sendFixedMessage("&8* &cOpuściłeś kolejke")
            }
        }
    }

    fun removeQueueWait(queueWait: QueueWait) {
        this.queuesWait.remove(queueWait.id)
        queueWait.queueEntries.asSequence().forEach {queueEntry ->
            queueEntry.players.forEach {
                this.plugin.profileManager.findProfile(it).profileState = ProfileState.LOBBY
            }
        }
    }

    fun getQueueWait(uniqueId: UUID) : QueueWait? {
        return this.queuesWait.values.singleOrNull { it.queueEntries.any { it.players.contains(uniqueId) } }
    }

    private fun getFreeQueueWait(queueEntry: QueueEntry, kitName: String, queueSize: QueueSize): QueueWait {
        var queueWait = this.queuesWait.values
                .filter { it.kitName == kitName }
                .filter { it.queueSize == queueSize }
                .find { it.getQueueEntriesSize() + queueEntry.size() <= queueSize.size }
        if (queueWait == null) {
            queueWait = QueueWait(this.plugin, this.ai.getAndIncrement(), kitName, queueSize)
            this.queuesWait[queueWait.id]= queueWait
        }
        return queueWait
    }

}