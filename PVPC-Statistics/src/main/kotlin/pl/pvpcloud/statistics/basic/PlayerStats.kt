package pl.pvpcloud.statistics.basic

import pl.pvpcloud.database.api.DatabaseEntity
import pl.pvpcloud.statistics.StatsAPI
import java.io.Serializable
import java.lang.Math.pow
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.pow
import kotlin.math.round

class PlayerStats(
        val uuid: UUID,
        var name: String,
        var points: Int,
        var kills: Int,
        var deaths: Int,
        var assists: Int,
        val customStats: ConcurrentHashMap<Int, CustomStats>
) : DatabaseEntity(), Serializable {

    var fakePoints: Int = 1000
    var lvls: Int = 0

    override val id: String get() = uuid.toString()
    override val collection: String get() = StatsAPI.plugin.config.collectionName
    override val key: String
        get() = "uuid"

    fun incrementValue(id: Int) {
        this.customStats[id]!!.value += 1
    }

    fun decrementValue(id: Int) {
        this.customStats[id]!!.value -= 1
    }

    fun getAndSetValue(id: Int, value: Int) {
        this.customStats[id]!!.value = value
    }

    fun getValue(id: Int): Int =
            this.customStats[id]!!.value

    fun getKD(): Double {
        return if (this.kills == 0 && this.deaths == 0) {
            0.0
        } else if (this.kills > 0 && this.deaths == 0) {
            this.kills.toDouble()
        } else if (this.deaths > 0 && this.kills == 0) {
            -this.deaths.toDouble()
        } else {
            val p = 10.0.pow(2.0)
            round((this.kills / this.deaths) * p) / p
        }
    }
}