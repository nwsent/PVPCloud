package pl.pvpcloud.moles.arena

import org.bukkit.Location
import org.bukkit.World

class Arena(
        val id: Int,
        private val arenaProfile: ArenaProfile,
        val worldName: String
) {
    var arenaState = ArenaState.NONE
    lateinit var world: World

    val worldBorderSize get() = this.arenaProfile.worldBorderSize
    val worldBorderMinSize get() = this.arenaProfile.worldBorderMinSize

    fun getAttackSpawn(): Location {
        return Location(this.world, this.arenaProfile.spawnAttack.x, this.arenaProfile.spawnAttack.y, this.arenaProfile.spawnAttack.z)
    }

    fun getDefendSpawn(): Location {
        return Location(this.world, this.arenaProfile.spawnDefend.x, this.arenaProfile.spawnDefend.y, this.arenaProfile.spawnDefend.z)
    }
}