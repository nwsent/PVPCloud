package pl.pvpcloud.save

import org.bukkit.entity.Player

object KitAPI {

    internal lateinit var kitModule: KitModule

    fun getKit(name: String): Kit {
        return this.kitModule.kitManager.getKit(name)
                ?: throw NullPointerException("Kit is null $name")
    }

    fun giveKit(player: Player, kit: Kit) {
        this.kitModule.kitManager.giveKit(player, kit)
    }
}