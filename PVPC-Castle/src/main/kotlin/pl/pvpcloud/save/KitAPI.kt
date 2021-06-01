package pl.pvpcloud.save

import org.bukkit.entity.Player

object KitAPI {

    internal lateinit var saveModule: SaveModule

    fun getKit(name: String, kitType: KitType): Kit {
        return this.saveModule.kitManager.getKit(name, kitType)
                ?: throw NullPointerException("Kit is null $name $kitType")
    }

    fun giveKit(player: Player, kit: Kit) {
        this.saveModule.kitManager.giveKit(player, kit)
    }
}