package pl.pvpcloud.castle.managers

import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.profile.ProfileState
import pl.pvpcloud.castle.queues.QueueEntry
import pl.pvpcloud.castle.queues.QueueThread
import pl.pvpcloud.castle.queues.QueueWait
import pl.pvpcloud.common.extensions.sendFixedMessage
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class QueueManager(private val plugin: CastlePlugin) {

    private val ai = AtomicInteger(0)
    val queuesWait = ConcurrentHashMap<Int, QueueWait>()
    val queuePlayer = ConcurrentHashMap<UUID, Int>() // uuid gracza, kolejka w jakiej jest
    val queueThread: QueueThread = QueueThread(this.plugin)

    fun addToQueue(player: Player) {
        this.plugin.profileManager.getProfile(player).also {
            if (!it.isState(ProfileState.LOBBY)) {
                return player.sendFixedMessage("&cJestes w kolejce")
            }
        }
        val queueEntry = QueueEntry(hashSetOf(player.uniqueId))
        val queueWait = this.getFreeQueueWait(queueEntry.size())
        queueWait.addEntry(queueEntry)
        queueEntry.getPlayers().forEach {
            this.queuePlayer[it.uniqueId] = queueWait.id
            this.plugin.profileManager.apply(it, ProfileState.QUEUING)
            it.sendFixedMessage("&8* &eDołączyłeś do kolejki")
        }
    }

    fun leaveFromQueue(player: Player) {
        val queueWait = this.queuesWait[this.queuePlayer[player.uniqueId]]
        if (queueWait != null) {
            val queueEntry = queueWait.getQueueEntry(player.uniqueId)
            queueWait.removeEntry(queueEntry)
        }
        this.queuePlayer.remove(player.uniqueId)
        this.plugin.profileManager.apply(player, ProfileState.LOBBY)
        player.sendFixedMessage("&8* &cOpuściłeś kolejke")
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
            if (queueWait.getPlayersSize() + size <= 250) {
                return queueWait
            }
        }
        val queueWait = QueueWait(this.plugin, this.ai.getAndIncrement())
        this.queuesWait[queueWait.id]= queueWait
        return queueWait
    }

}