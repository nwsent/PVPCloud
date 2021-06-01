package pl.pvpcloud.grouptp.arena.arena

data class ArenaProfile(
        val name: String,
        val worldBorderSize: Double,
        val worldBorderMinSize: Double,
        val cuboid: Cuboid,
        val spawn: ArenaLocation
)