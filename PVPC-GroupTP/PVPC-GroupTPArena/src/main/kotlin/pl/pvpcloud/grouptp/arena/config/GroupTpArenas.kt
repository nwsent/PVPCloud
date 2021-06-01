package pl.pvpcloud.grouptp.arena.config

import pl.pvpcloud.grouptp.arena.arena.ArenaLocation
import pl.pvpcloud.grouptp.arena.arena.ArenaProfile
import pl.pvpcloud.grouptp.arena.arena.Cuboid

class GroupTpArenas {

    val arenas = arrayListOf(
            ArenaProfile(
                    "Pustynia",
                    250.0,
                    100.0,
                    Cuboid(10, 10, 64, 5),
                    ArenaLocation(0.0, 64.0, 0.0)
            )
    )
}