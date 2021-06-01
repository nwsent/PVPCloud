package pl.pvpcloud.save

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemHelper

class SaveConfig {

    val sections = arrayListOf(
            SaveSectionHelper(
                    "IRON",
                    SlotPos(1, 2),
                    ItemHelper("&fIRON", Material.IRON_SWORD.id, 1, 0, arrayListOf("", "&7Kiknij żeby edytować ten zestaw!"))
            )
    )
}