package pl.pvpcloud.moles.arena

data class ArenaProfile(
        val name: String,
        val worldBorderSize: Double,
        val worldBorderMinSize: Double,
        val spawnAttack: ArenaLocation,
        val spawnDefend: ArenaLocation
)