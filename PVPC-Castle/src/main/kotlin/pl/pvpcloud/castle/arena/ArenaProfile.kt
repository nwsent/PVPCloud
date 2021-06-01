package pl.pvpcloud.castle.arena

import pl.pvpcloud.common.builders.ItemBuilder

data class ArenaProfile(
    val name: String,
    val icon: ItemBuilder,
    val worldBorderSize: Double,
    val worldBorderMinSize: Double,
    val cuboid: Cuboid,
    val spawnAttack: ArenaLocation,
    val spawnDefend: ArenaLocation
)