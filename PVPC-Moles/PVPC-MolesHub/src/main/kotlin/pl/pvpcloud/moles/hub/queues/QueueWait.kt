package pl.pvpcloud.moles.hub.queues

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.moles.common.packets.PacketPrepareMatch
import pl.pvpcloud.moles.common.packets.PacketStatusRequest
import pl.pvpcloud.moles.common.packets.PacketStatusResponse
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.NatsPacket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class QueueWait(
        private val plugin: MolesPlugin,
        val id: Int
) {
    val queueEntries = HashSet<QueueEntry>()

    private val attackTeam = HashSet<UUID>()
    private val defendTeam = HashSet<UUID>()

    var isLock = false
    var countdown = 30

    fun doTick() {
        if (getPlayersSize() < this.plugin.config.minToStart || this.queueEntries.size < 2) {
            if (countdown < 10) {
                countdown = 15
                this.isLock = false
                sendTitle("&cZbyt mała ilość graczy, przerywam odliczanie!")
            }
            return
        }
        when {
           decrementCountdown() == 0 -> {
                this.isLock = true
                this.sendRequest()
                return
            }
            countdown > 0 -> {
                sendTitle("&eArena rozpocznie się za&8: &f${this.countdown}")
            }
            else -> {
                if (this.countdown % 5 == 0) {
                    sendTitle("&cPoczekaj chwile! (Prawdopodobnie padł serwer)")
                    sendMessage("&cPoczekaj chwile! (Prawdopodobnie padł serwer)")
                    this.countdown = 15
                    this.isLock = false
                }
            }
        }
    }

    private fun sendRequest() {
        NetworkAPI.requestAndResponse("moles_1", { PacketStatusRequest() }, {
            if (it is PacketStatusResponse) {
                this.sendMatch()
            }
        })
    }

    private fun sendMatch() {
        this.plugin.queueManager.removeQueueWait(this)
        this.sortingPlayers()
        val packet = PacketPrepareMatch(this.attackTeam.mapNotNull { Bukkit.getPlayer(it).uniqueId }.toSet(), this.defendTeam.mapNotNull { Bukkit.getPlayer(it).uniqueId }.toSet())
        NetworkAPI.publish("moles_1") { packet }
    }

    private fun sortingPlayers() {
        val qN = HashSet(this.queueEntries)
        this.attackTeam.clear()
        this.defendTeam.clear()

        while (qN.isNotEmpty()) {
            if (getAttackSize() < getDefendSize()) {
                val qM = getMaximumEntry(qN)
                this.attackTeam.addAll(qM.players)
                qN.remove(qM)
            }
            if (qN.isNotEmpty() && getDefendSize() <= getAttackSize()) {
                val qM = getMaximumEntry(qN)
                this.defendTeam.addAll(qM.players)
                qN.remove(qM)
            }
        }
    }

    fun getPlayersSize(): Int {
        return this.queueEntries.asSequence().sumBy { it.size() }
    }

    private fun decrementCountdown(): Int {
        return --this.countdown
    }

    private fun sendTitle(subTitle: String) {
        this.queueEntries.asSequence().forEach {
            it.getPlayers().forEach { player ->
                player.sendTitle("", subTitle, 0, 40, 0)
            }
        }
    }

    private fun sendMessage(message: String) {
        this.queueEntries.asSequence().forEach {
            it.getPlayers().forEach { player ->
                player.sendFixedMessage(message)
            }
        }
    }

    private fun getMaximumEntry(queueEntries: HashSet<QueueEntry>): QueueEntry {
        return queueEntries.maxBy { it.size() }
                ?: throw NullPointerException("sortowanie nie ma maxBy")
    }

    private fun getAttackSize(): Int {
        return this.attackTeam.size
    }

    private fun getDefendSize(): Int {
        return this.defendTeam.size
    }
}