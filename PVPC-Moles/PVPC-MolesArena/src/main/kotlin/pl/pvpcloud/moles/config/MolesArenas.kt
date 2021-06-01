package pl.pvpcloud.moles.config

import pl.pvpcloud.moles.arena.ArenaLocation
import pl.pvpcloud.moles.arena.ArenaProfile

class MolesArenas {

    val arenas = arrayListOf(
            ArenaProfile(
                    "Pustynia",
                    250.0,
                    100.0,
                    ArenaLocation(0.0, 64.0, 10.0),
                    ArenaLocation(0.0, 64.0, 10.0)
            )
    )
}