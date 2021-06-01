package pl.pvpcloud.xvsx.hub.queues

import org.bukkit.Bukkit
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.xvsx.common.packets.PacketPrepareMatch
import pl.pvpcloud.xvsx.common.packets.PacketStatusRequest
import pl.pvpcloud.xvsx.common.packets.PacketStatusResponse
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class QueueWait(
        val plugin: XvsXPlugin,
        val id: Int,
        val kitName: String,
        val queueSize: QueueSize
) {

    val queueEntries = HashSet<QueueEntry>()

    private val teamA = HashSet<UUID>()
    private val teamB = HashSet<UUID>()

    var isLock = false
    var countdown = 6

    var createTime = System.currentTimeMillis()

    fun doTick() {
        val queueEntriesSize = this.getQueueEntriesSize()

        if (queueEntriesSize < this.queueSize.size) {
            this.sendTitle("&cSzukanie graczy")
            this.isLock = false
        } else {
            this.isLock = true
            this.sendTitle("&ePrzygotowania")
            --this.countdown
            if (this.countdown == 5) {
                this.sendRequest()
            }
            if (this.countdown == 0) {
                sendTitle("&cPoczekaj chwile! (Prawdopodobnie padł serwer)")
                sendMessage("&cPoczekaj chwile! (Prawdopodobnie padł serwer)")
                this.countdown = 5
            }
        }
    }

    private fun sendRequest() {
        NetworkAPI.requestAndResponse("xvsx_1", { PacketStatusRequest() }, {
            if (it is PacketStatusResponse) {
                this.sendMatch()
            }
        })
    }

    private fun sendMatch() {
        this.plugin.queueManager.removeQueueWait(this)
        this.sortingPlayers()
        NetworkAPI.publish("xvsx_1") {
            PacketPrepareMatch(
                    this.kitName,
                    1,
                    this.queueSize.size,
                    this.teamA.mapNotNull { Bukkit.getPlayer(it).uniqueId }.toSet(),
                    this.teamB.mapNotNull { Bukkit.getPlayer(it).uniqueId }.toSet()
            )
        }
    }

    private fun sortingPlayers() {
        val qN = this.queueEntries.toMutableList()

        while (qN.isNotEmpty()) {
            if (getTeamASize() < getTeamBSize()) { //todo <= chyba bo zawsze leci ten drugi if jak pierwszy (Z testów wynika ze jest ok)
                val qM = getMaximumEntry(qN)
                this.teamA.addAll(qM.players)
                qN.remove(qM)
            }
            if (qN.isNotEmpty() && getTeamBSize() <= getTeamASize()) {
                val qM = getMaximumEntry(qN)
                this.teamB.addAll(qM.players)
                qN.remove(qM)
            }
        }
    }

    fun getQueueEntriesSize(): Int =
            this.queueEntries.map { it.size() }.sum()

    private fun getMaximumEntry(queueEntries: MutableList<QueueEntry>): QueueEntry =
            queueEntries.maxBy { it.size() }
                ?: throw NullPointerException("sortowanie nie ma maxBy")

    private fun getTeamASize(): Int = this.teamA.size

    private fun getTeamBSize(): Int = this.teamB.size

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
}