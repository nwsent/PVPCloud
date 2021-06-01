package pl.pvpcloud.moles.managers

import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.profile.ProfileState
import pl.pvpcloud.moles.queues.QueueEntry
import pl.pvpcloud.moles.queues.QueueThread
import pl.pvpcloud.moles.queues.QueueWait
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class QueueManager(private val plugin: MolesPlugin) {

    private val ai = AtomicInteger(0)
    val queuesWait = ConcurrentHashMap<Int, QueueWait>()
    private val queuePlayer = ConcurrentHashMap<UUID, Int>() // uuid gracza, kolejka w jakiej jest
    val queueThread = QueueThread(this.plugin)

    fun addToQueue(player: Player) {
        val party = this.plugin.partyManager.getParty(player.uniqueId)
        val queueEntry: QueueEntry =
                if (party != null) {
                    if (!party.isLeader(player.uniqueId)) {
                        return player.sendFixedMessage("&4* &cTylko lider może dołączyć do kolejki")
                    }
                    if (!party.allPlayersState(ProfileState.LOBBY)) {
                        return player.sendFixedMessage("&4* &cNie wszyscy gracze z party są w lobby")
                    }
                    QueueEntry(party.members)
                } else {
                    this.plugin.profileManager.getProfile(player).also {
                        if (!it.isState(ProfileState.LOBBY)) {
                            return player.sendFixedMessage("&cJestes w kolejce")
                        }
                    }
                    QueueEntry(hashSetOf(player.uniqueId))
                }
        val queueWait = this.getFreeQueueWait(queueEntry.size())
        queueWait.addEntry(queueEntry)
        queueEntry.getPlayers().forEach {
            this.queuePlayer[it.uniqueId] = queueWait.id
            this.plugin.profileManager.getProfile(it).also {
                it.profileState = ProfileState.QUEUING
            }
            this.plugin.profileManager.refreshHotbar(it)
            it.sendFixedMessage("&8* &eDołączyłeś do kolejki")
        }
    }

    fun leaveFromQueue(player: Player) {
        val queueWait = this.queuesWait[this.queuePlayer[player.uniqueId]]
        if (queueWait != null) {
            val queueEntry = queueWait.getQueueEntry(player.uniqueId)
            queueWait.removeEntry(queueEntry)
        }

        val party = this.plugin.partyManager.getParty(player.uniqueId)
        if (party != null) {
            if (!party.isLeader(player.uniqueId)) {
                return player.sendFixedMessage("&4* &cTylko lider może opuścić kolejke.")
            }
            party.getPlayers().forEach {
                this.queuePlayer.remove(it.uniqueId)
                this.plugin.profileManager.apply(it, ProfileState.LOBBY)
            }
            party.sendMessage("&8* &cTwoje party opuściło kolejkę")
        } else {
            this.queuePlayer.remove(player.uniqueId)
            this.plugin.profileManager.apply(player, ProfileState.LOBBY)
            player.sendFixedMessage("&8* &cOpuściłeś kolejke")
        }
    }

    fun removeQueueWait(queueWait: QueueWait) {
        this.queuesWait.remove(queueWait.id)
        queueWait.queueEntries.values.forEach {queueEntry ->
            queueEntry.players.forEach {
                this.queuePlayer.remove(it)
            }
        }
    }

    fun getQueueWait(uniqueId: UUID) : QueueWait? {
        return this.queuesWait[this.queuePlayer[uniqueId]]
    }

    private fun getFreeQueueWait(size: Int): QueueWait {
        for (queueWait in this.queuesWait.values) {
            if (queueWait.getPlayersSize() + size <= 32) {
                return queueWait
            }
        }
        val queueWait = QueueWait(this.plugin, this.ai.getAndIncrement())
        this.queuesWait[queueWait.id]= queueWait
        return queueWait
    }

}