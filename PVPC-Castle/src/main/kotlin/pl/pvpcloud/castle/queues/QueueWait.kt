package pl.pvpcloud.castle.queues

import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.castle.arena.Arena
import pl.pvpcloud.castle.match.Match
import pl.pvpcloud.castle.match.MatchTeam
import pl.pvpcloud.common.extensions.sendTitle
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
class QueueWait(
        private val plugin: CastlePlugin,
        val id: Int
) {

    private val arenaVotes: MutableMap<UUID, String> = HashMap()

    val queueEntries = ConcurrentHashMap<UUID, QueueEntry>()
    private val attackTeam = MatchTeam(0)
    private val defendTeam = MatchTeam(1)

    @Volatile var countdown = 30

    lateinit var arena: Arena

    init {
        this.arenaVotes[UUID.randomUUID()] = this.plugin.arenaManager.arenasProfile.random().name
    }

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
            this.countdown == 5 -> {
                if (!::arena.isInitialized)
                    this.handleEndVote()
            }
            this.countdown > 0 ->
                sendTitle("", "&eArena rozpocznie się za&8: &f${this.countdown}")
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

        val qA = qECopy.values.filter { it.queueType == QueueType.ATTACK }.toMutableList()
        val qD = qECopy.values.filter { it.queueType == QueueType.DEFEND }.toMutableList()
        val qN = qECopy.values.filter { it.queueType == QueueType.NONE }.toMutableList()

        while (qA.isNotEmpty() || qD.isNotEmpty() || qN.isNotEmpty()) {
            if (qA.isNotEmpty()) {
                if (getAttackSize() <= getDefendSize()) {
                    val qM = getMaximumEntry(qA)
                    this.attackTeam.add(qM)
                    qA.remove(qM)
                } else {
                    qN.addAll(qA)
                    qA.clear()
                }
            }
            if (qD.isNotEmpty()) {
                if (getDefendSize() <= getAttackSize()) {
                    val qM = getMaximumEntry(qD)
                    this.defendTeam.add(qM)
                    qD.remove(qM)
                } else {
                    qN.addAll(qD)
                    qD.clear()
                }
            }
            if (qD.isEmpty() && qA.isEmpty()) {
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
    }

    fun handleVote(uniqueId: UUID, arenaProfileName: String) {
        this.arenaVotes[uniqueId] = arenaProfileName
    }

    fun handleEndVote() {
        val counts: MutableMap<String, Int> = HashMap()
        for (str in this.arenaVotes.values) {
            if (counts.containsKey(str)) {
                counts[str] = counts[str]!! + 1
            } else {
                counts[str] = 1
            }
        }
        this.arena = this.plugin.arenaManager.getFreeArena(counts.maxBy { it.value }!!.key)
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