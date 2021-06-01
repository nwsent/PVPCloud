package pl.pvpcloud.standard.helper

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework

object FireworkHelper {

    fun spawnFirework(location: Location) {
        val firework = location.world.spawnEntity(location, EntityType.FIREWORK) as Firework
        val fireworkMeta = firework.fireworkMeta
        val effectBall = FireworkEffect.builder().withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build()

        fireworkMeta.addEffects(effectBall)
        fireworkMeta.power = 1
        firework.fireworkMeta = fireworkMeta
    }

}
