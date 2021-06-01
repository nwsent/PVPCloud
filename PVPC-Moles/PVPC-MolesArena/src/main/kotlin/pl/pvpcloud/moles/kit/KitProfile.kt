package pl.pvpcloud.moles.kit

import pl.pvpcloud.common.helpers.ItemEnchantedHelper

data class KitProfile(
        val name: String,
        val item: ItemEnchantedHelper,
        var kits: HashSet<Kit>
)