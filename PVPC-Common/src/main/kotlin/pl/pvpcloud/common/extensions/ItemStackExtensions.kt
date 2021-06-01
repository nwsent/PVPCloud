package pl.pvpcloud.common.extensions

import org.bukkit.Material

fun Material.bukkitName(): String = name.toLowerCase().split("_").map { it.capitalize() }.joinToString { it }.rep(",", "")