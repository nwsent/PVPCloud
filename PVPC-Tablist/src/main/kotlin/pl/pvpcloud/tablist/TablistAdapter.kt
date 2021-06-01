package pl.pvpcloud.tablist

import org.bukkit.entity.Player

abstract class TablistAdapter(val name: String) {

    abstract fun replace(player: Player): String

}