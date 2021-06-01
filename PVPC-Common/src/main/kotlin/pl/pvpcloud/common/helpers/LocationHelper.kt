package pl.pvpcloud.common.helpers

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import java.util.*

data class LocationHelper(
        var world: String,
        val x: Double,
        val y: Double,
        val z: Double,
        var yaw: Float,
        var pitch: Float
) {

    constructor(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) : this("world", x, y, z, yaw, pitch)

    constructor(x: Double, y: Double, z: Double) : this("world", x, y, z, 0.0f, 0.0f)

    constructor(world: String, x: Double, y: Double, z: Double) : this(world, x, y, z, 0.0f, 0.0f)

    companion object {

        fun fromBukkitLocation(location: Location): LocationHelper {
            return LocationHelper(location.world.name, location.x, location.y, location.z, location.yaw, location.pitch)
        }

        fun locationToString(loc: LocationHelper): String {
            val joiner = StringJoiner(", ")
            joiner.add(loc.x.toString())
            joiner.add(loc.y.toString())
            joiner.add(loc.z.toString())
            if (loc.yaw == 0.0f && loc.pitch == 0.0f) {
                if (loc.world == "world") {
                    return joiner.toString()
                }
                joiner.add(loc.world)
                return joiner.toString()
            } else {
                joiner.add(loc.yaw.toString())
                joiner.add(loc.pitch.toString())
                if (loc.world == "world") {
                    return joiner.toString()
                }
                joiner.add(loc.world)
                return joiner.toString()
            }
        }

        fun stringToLocation(string: String): LocationHelper {
            val split = string.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val x = split[0].toDouble()
            val y = split[1].toDouble()
            val z = split[2].toDouble()
            val customLocation = LocationHelper(x, y, z)
            if (split.size == 4) {
                customLocation.world = split[3]
            } else if (split.size >= 5) {
                customLocation.yaw = split[3].toFloat()
                customLocation.pitch = split[4].toFloat()
                if (split.size >= 6) {
                    customLocation.world = split[5]
                }
            }
            return customLocation
        }
    }

    fun toBukkitLocation(): Location {
        return Location(toBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch)
    }

    fun toBukkitWorld(): World {
        return Bukkit.getServer().getWorld(this.world)
    }
}