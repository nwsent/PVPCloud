package pl.pvpcloud.addons.profile.colorname

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.helpers.ItemHelper

enum class ColorType(val colorName: String, val slotPos: SlotPos, val itemHelper: ItemBuilder) {

    GRAY("&fSzary", SlotPos(1,2), ItemBuilder(Material.STAINED_CLAY, 1, 0, "&8* &fSzary &8*")),
    WHITE("&fBiały", SlotPos(1,2), ItemBuilder(Material.STAINED_CLAY, 1, 0, "&8* &fBiały &8*")),
    YELLOW("&eŻółty", SlotPos(1,3), ItemBuilder(Material.STAINED_CLAY,1, 4,"&8* &eŻółty &8*")),
    GREEN("&aZielony", SlotPos(1,4), ItemBuilder(Material.STAINED_CLAY, 1, 5, "&8* &aZielony &8*")),
    BLUE("&bNiebieski", SlotPos(1,5), ItemBuilder(Material.STAINED_CLAY, 1, 3, "&8* &bNiebieski &8*")),
    LIGHT_PURPLE("&dRóżowy", SlotPos(1,6), ItemBuilder(Material.STAINED_CLAY,1, 2, "&8* &dRóżowy &8*"))

}