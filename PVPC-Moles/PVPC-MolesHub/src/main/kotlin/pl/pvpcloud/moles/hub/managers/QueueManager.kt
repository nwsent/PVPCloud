package pl.pvpcloud.moles.hub.managers

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.moles.hub.profile.ProfileState
import pl.pvpcloud.moles.hub.queues.QueueEntry
import pl.pvpcloud.moles.hub.queues.QueueThread
import pl.pvpcloud.moles.hub.queues.QueueWait
import pl.pvpcloud.party.PartyAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class QueueManager(private val plugin: MolesPlugin) {

    private val ai = AtomicInteger(0)
    val queuesWait = ConcurrentHashMap<Int, QueueWait>()

    val queueThread = QueueThread(this.plugin)

    fun addToQueue(player: Player) {
        val profile = this.plugin.profileManager.findProfile(player)
        if (!profile.isState(ProfileState.LOBBY)) {
            return player.sendFixedMessage("&cJestes w kolejce")
        }
        val party = PartyAPI.getParty(player.uniqueId)
        val queueEntry = if (party != null) {
            if (party.uniqueIdLeader != player.uniqueId) {
                player.sendFixedMessage("&cNie jesteś liderem")
                return
            }
            if (party.membersOnline().none { this.plugin.profileManager.getProfile(it)!!.isState(ProfileState.LOBBY) }) {
                player.sendFixedMessage("&cNie wszyscy gracze sa w lobby")
                return
            }
            QueueEntry(party.membersOnline().map { it.uniqueId }.toHashSet())
        } else {
             QueueEntry(hashSetOf(player.uniqueId))
        }
        val queueWait = this.getFreeQueueWait(queueEntry.size())
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

    private fun getFreeQueueWait(size: Int): QueueWait {
        for (queueWait in this.queuesWait.values) {
            if (!queueWait.isLock && queueWait.getPlayersSize() + size <= 16) {
                return queueWait
            }
        }
        val queueWait = QueueWait(this.plugin, this.ai.getAndIncrement())
        this.queuesWait[queueWait.id]= queueWait
        return queueWait
    }

}