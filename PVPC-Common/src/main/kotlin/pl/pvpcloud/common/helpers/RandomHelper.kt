package pl.pvpcloud.common.helpers

import java.util.concurrent.ThreadLocalRandom

object RandomHelper {

    private const val characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    private val generator = ThreadLocalRandom.current()

    fun getRandomInt(min: Int, max: Int) = generator.nextInt(max - min + 1) + min

    private fun getRandomDouble(min: Double, max: Double) = generator.nextDouble() * (max - min) + min

    fun getChance(chance: Double) = chance >= 100 || chance >= getRandomDouble(0.0, 100.0)

    fun getChance(chance: Int) = chance >= 100 || chance >= getRandomDouble(0.0, 100.0)

    fun getRandomString(len: Int): String {
        val builder = StringBuilder(len)
        for (i in 0 until len) {
            builder.append(characters[getRandomInt(0, characters.length - 1)])
        }
        return builder.toString()
    }

    fun getRandomStringWithoutNumbers(len: Int): String {
        val builder = StringBuilder(len)
        for (i in 0 until len) {
            builder.append(characters[getRandomInt(10, characters.length - 1)])
        }
        return builder.toString()
    }

    fun getRandomStringNumber(len: Int): String {
        val builder = StringBuilder(len)
        for (i in 0 until len) {
            builder.append(characters[getRandomInt(0, 9)])
        }
        return builder.toString()
    }
}