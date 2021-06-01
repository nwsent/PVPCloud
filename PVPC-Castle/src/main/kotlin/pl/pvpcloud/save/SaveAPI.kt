package pl.pvpcloud.save

import org.bukkit.entity.Player
import pl.pvpcloud.shop.ShopAPI

object SaveAPI {

    internal lateinit var saveModule: SaveModule

    fun giveItems(player: Player, kitName: String, kitType: KitType) {
        this.saveModule.saveManager.giveItems(player, kitName, kitType)
        ShopAPI.giveItems(player)
    }

    fun openSaveGui(player: Player) {
        SaveGuiSelectTeam.getInventory(this.saveModule).open(player)
    }
}