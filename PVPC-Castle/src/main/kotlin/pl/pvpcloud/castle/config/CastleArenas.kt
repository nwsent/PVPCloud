package pl.pvpcloud.castle.config

import org.bukkit.Material
import pl.pvpcloud.castle.arena.ArenaLocation
import pl.pvpcloud.castle.arena.ArenaProfile
import pl.pvpcloud.castle.arena.Cuboid
import pl.pvpcloud.common.builders.ItemBuilder

class CastleArenas {

    val arenas = arrayListOf(
        ArenaProfile(
            "Pustynia",
            ItemBuilder(Material.SAND, "&8* &ePustyna &8*", arrayListOf(" ", " &8* &fWybierz swoją ulubią mape!", " ")),
            250.0,
            100.0,
            Cuboid(-10, 10, 5, 5),
            ArenaLocation(0.0, 64.0, 10.0),
            ArenaLocation(0.0, 64.0, 10.0)
        )
    )

}