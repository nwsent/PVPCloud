package pl.pvpcloud.moles.queues

import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.moles.match.MatchTeam
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class QueueWait(
    private val plugin: MolesPlugin,
    val id: Int
) {

    val queueEntries = ConcurrentHashMap<UUID, QueueEntry>()
    private val attackTeam = MatchTeam(0)
    private val defendTeam = MatchTeam(1)
    var countdown = 30

    val arena = this.plugin.arenaManager.getFreeArena()

    fun doTick() {
        if (getPlayersSize() < this.plugin.config.minToStart || this.queueEntries.size < 2) {
            if (countdown < 10) {
                countdown = 15
                sendTitle("", "&cZbyt mała ilość graczy, przerywam odliczanie!")
            }
            return
        }
        when {
            this.decrementCountdown() == 0 -> {
                this.prepare()
                return
            }
            this.countdown > 0 ->
                sendTitle("", "&eArena rozpocznie się za&8: &f${this.countdown}")
            else -> {
                if (this.countdown % 5 == 0) {
                    sendTitle("&cPoczekaj chwile!", "&fSzukanie wolnej areny")
                    this.prepare()
                }
            }
        }
    }

    private fun prepare() {
        this.plugin.queueManager.removeQueueWait(this)
        this.arena.world.worldBorder.setSize(arena.worldBorderMinSize, TimeUnit.MINUTES.toSeconds(10))
        this.sortingPlayers()
        val match = Match(this.plugin, this.arena, this.attackTeam, this.defendTeam, UUID.randomUUID())
        this.plugin.matchManager.createMatch(match)
    }

    fun addEntry(queueEntry: QueueEntry) {
        this.queueEntries[queueEntry.players.first()] = queueEntry
    }

    fun removeEntry(queueEntry: QueueEntry) {
        this.queueEntries.remove(queueEntry.players.first())
    }

    fun getQueueEntry(uuid: UUID): QueueEntry {
        return this.queueEntries.values.single { it.players.contains(uuid) }
    }

    private fun sortingPlayers() {
        val qECopy = HashMap(this.queueEntries)

        val qN = qECopy.values.toMutableList()

        while (qN.isNotEmpty()) {
            if (getAttackSize() < getDefendSize()) {
                val qM = getMaximumEntry(qN)
                this.attackTeam.add(qM)
                qN.remove(qM)
            }
            if (qN.isNotEmpty() && getDefendSize() <= getAttackSize()) {
                val qM = getMaximumEntry(qN)
                this.defendTeam.add(qM)
                qN.remove(qM)
            }
        }
    }

    fun getPlayersSize(): Int {
        return this.queueEntries.values.stream().mapToInt { it.size() }.sum()
    }

    fun decrementCountdown(): Int {
        return --this.countdown
    }

    fun sendTitle(title: String, subTitle: String) {
        this.queueEntries.values.forEach {
            it.getPlayers().forEach { player ->
                player.sendTitle(title, subTitle, 0, 40, 0)
            }
        }
    }

    // Sortowanie Funkcje

    private fun getMaximumEntry(queueEntries: MutableList<QueueEntry>): QueueEntry {
        return queueEntries.maxBy { it.size() }
                ?: throw NullPointerException("sortowanie nie ma maxBy")
    }

    private fun getAttackSize(): Int {
        return this.attackTeam.size()
    }

    private fun getDefendSize(): Int {
        return this.defendTeam.size()
    }
}