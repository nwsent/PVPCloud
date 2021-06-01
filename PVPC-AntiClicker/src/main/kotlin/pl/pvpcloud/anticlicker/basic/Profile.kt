package pl.pvpcloud.anticlicker.basic

import org.bukkit.Bukkit
import java.util.*
import kotlin.collections.ArrayList

class Profile(
        val uniqueId: UUID,
        val lastHits: ArrayList<Long>,
        var lastHit: Long
) {

    fun calc() {
        val lastHitTime = System.currentTimeMillis() - lastHit
        if (lastHitTime <= 10) {
            Bukkit.broadcastMessage("Anty-Makro: $lastHitTime")
        }
        this.lastHit = System.currentTimeMillis()
        this.lastHits.add(lastHitTime)
        if (this.lastHits.size >= 15) {
            val avg = this.lastHits.average()
            if (avg < 65) {
                Bukkit.broadcastMessage("Anty-Makro (AVG): $avg")
            }
            this.lastHits.clear()
        }
    }

}