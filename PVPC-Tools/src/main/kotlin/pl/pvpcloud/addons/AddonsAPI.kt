package pl.pvpcloud.addons

import org.bukkit.Material
import pl.pvpcloud.common.builders.HeadBuilder
import pl.pvpcloud.common.builders.ItemBuilder

object AddonsAPI {

    internal lateinit var module: AddonsModule

    val HUB =
            ItemBuilder(Material.DARK_OAK_FENCE_GATE, 1, "&8* &cWróć na hub'a &8*").build()

    fun HEAD(string: String) =
        HeadBuilder(string, "&8* &eTwoj profil &8*").build()

}