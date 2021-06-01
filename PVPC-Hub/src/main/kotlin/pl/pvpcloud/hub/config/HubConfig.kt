package pl.pvpcloud.hub.config

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.common.helpers.LocationHelper
import pl.pvpcloud.hub.mode.Mode

class HubConfig {

    val modes = arrayListOf(
            Mode("castleMod", ItemHelper("Castle Mod", Material.TNT.id, 1, 0, arrayListOf("Siema")), SlotPos(1, 2), arrayListOf("moles")),
            Mode("moles", ItemHelper("Krety", Material.IRON_PICKAXE.id, 1, 0, arrayListOf("Siema")), SlotPos(1, 4), arrayListOf("moles"))
    )

    val lobbyLocation = LocationHelper(0.0, 65.0, 0.0, 0.0f, 0.0f)

    val itemSelectMode = ItemHelper("&8* &aWybierz serwer &8*", Material.COMPASS.id)
    val itemEnderPearl = ItemHelper("&8* &5Perełka &8*", Material.ENDER_PEARL.id)

}