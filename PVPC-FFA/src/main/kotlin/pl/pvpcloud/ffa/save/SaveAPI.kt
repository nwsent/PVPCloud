package pl.pvpcloud.ffa.save

import org.bukkit.entity.Player

object SaveAPI {

    internal lateinit var saveModule: SaveModule

    fun giveItems(player: Player, kitName: String) {
        saveModule.saveManager.giveItems(player, kitName)
        saveModule.plugin.purchasesManager.validShopItems(player)
    }
}