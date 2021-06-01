package pl.pvpcloud.moles.config

import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemEnchantedHelper
import pl.pvpcloud.moles.kit.KitProfile

class MolesKits {

    val kitsProfiles = arrayListOf(
            KitProfile(
                    "IronP1",
                    ItemEnchantedHelper("&8* &6Iron &8*", Material.IRON_SWORD.id, 1, 0, arrayListOf("Iron Set", "Prot 1")),
                    hashSetOf()
            )
    )
}