package pl.pvpcloud.castle.arena

import org.bukkit.Location
import org.bukkit.World
import kotlin.math.abs

class Arena(
    val id: Int,
    private val arenaProfile: ArenaProfile,
    val worldName: String
) {
    lateinit var world: World

    val worldBorderSize get() = this.arenaProfile.worldBorderSize
    val worldBorderMinSize get() = this.arenaProfile.worldBorderMinSize

    fun isInCuboid(loc: Location): Boolean {
        if (loc.world != this.world) {
            return false
        }
        val dx = abs(loc.blockX - this.arenaProfile.cuboid.x)
        val dz = abs(loc.blockZ - this.arenaProfile.cuboid.z)
        return dx <= this.arenaProfile.cuboid.size && dz <= this.arenaProfile.cuboid.size
    }

    fun isInCuboidByLoc(loc: Location): Boolean {
        if (loc.world != this.world) {
            return false
        }
        val dx = abs(loc.blockX - this.arenaProfile.cuboid.x)
        val dz = abs(loc.blockZ - this.arenaProfile.cuboid.z)
        return dx - 1 <= this.arenaProfile.cuboid.size && dz - 1 <= this.arenaProfile.cuboid.size
    }

    fun isInCenter(loc: Location, top: Int, down: Int, wall: Int): Boolean {
        val c = this.getLocation().clone()
        return (c.blockY - down <= loc.blockY && c.blockY + top >= loc.blockY &&
                loc.blockX <= c.blockX + wall
                && loc.blockX >= c.blockX - wall
                && loc.blockZ <= c.blockZ + wall
                && loc.blockZ >= c.blockZ - wall)
    }

    fun getAttackSpawn(): Location {
        return Location(this.world, this.arenaProfile.spawnAttack.x, this.arenaProfile.spawnAttack.y, this.arenaProfile.spawnAttack.z)
    }

    fun getDefendSpawn(): Location {
        return Location(this.world, this.arenaProfile.spawnDefend.x, this.arenaProfile.spawnDefend.y, this.arenaProfile.spawnDefend.z)
    }

    private fun getLocation(): Location {
        return Location(this.world, this.arenaProfile.cuboid.x.toDouble(), this.arenaProfile.cuboid.y.toDouble(), this.arenaProfile.cuboid.z.toDouble())
    }
}