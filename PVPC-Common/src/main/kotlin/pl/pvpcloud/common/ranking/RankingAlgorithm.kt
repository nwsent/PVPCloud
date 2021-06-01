package pl.pvpcloud.common.ranking

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class RankingAlgorithm(private val multiplier: Int) {

    fun calculateKiller(firstPoints: Int, secondPoints: Int): Int {
        return ceil(firstPoints + this.multiplier * (1 - 1 / (1 + 10.0.pow((secondPoints - firstPoints) / 400.0)))).toInt()
    }

    fun calculateVictim(firstPoints: Int, secondPoints: Int): Int {
        return floor(secondPoints + this.multiplier * (0 - 1 / (1 + 10.0.pow((firstPoints - secondPoints) / 400.0)))).toInt()
    }

}