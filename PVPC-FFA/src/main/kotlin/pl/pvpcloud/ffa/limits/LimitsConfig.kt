package pl.pvpcloud.ffa.limits

import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.ffa.limits.deposit.ConfigDeposit

class LimitsConfig {

    val limits = listOf(
        ConfigDeposit(Material.GOLDEN_APPLE.id, 1, 1, ItemHelper("&eKOX!", Material.GOLDEN_APPLE.id, 1, 1, listOf("", "&7Posiadasz: &e%deposit%")), "&8* &4-%consume% &ckoxow, limit to &4%limit%"),
        ConfigDeposit(Material.GOLDEN_APPLE.id, 0, 20, ItemHelper("&eREF!", Material.GOLDEN_APPLE.id, 1, 0, listOf("", "&7Posiadasz: &e%deposit%")), "&8* &4-%consume% &crefow, limit to &4%limit%"),
        ConfigDeposit(Material.ENDER_PEARL.id, 0, 3, ItemHelper("&5PERLA!", Material.ENDER_PEARL.id, 1, 0, listOf("", "&7Posiadasz: &e%deposit%")), "&8* &4-%consume% &cperel, limit to &4%limit%")
    )

    val pullOneItem = "&8* &cPozostało Ci &4%total%"
    val cantPullNoItems = "&4* &cPusto!"
    val youHaveLimitThisInInventory = "&4* &cPosiadasz limit!"
}