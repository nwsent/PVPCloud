package pl.pvpcloud.addons.profile.particle

import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.Location
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object ParticleHelper {

    val random = Random(System.nanoTime())

    fun getRandomVector(): Vector {
        val x: Double = random.nextDouble() * 2 - 1
        val y: Double = random.nextDouble() * 2 - 1
        val z: Double = random.nextDouble() * 2 - 1
        return Vector(x, y, z).normalize()
    }

    fun getRandomCircleVector(): Vector {
        val rnd: Double = random.nextDouble() * 2 * Math.PI
        return Vector(cos(rnd), 0.0, sin(rnd))
    }

}