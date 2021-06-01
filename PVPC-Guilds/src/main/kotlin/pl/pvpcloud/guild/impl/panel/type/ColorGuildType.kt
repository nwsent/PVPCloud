package pl.pvpcloud.guild.impl.panel.type

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.builders.ItemBuilder

enum class ColorGuildType(val designation: String, val slotPos: SlotPos, val int: Int, val itemBuilder: ItemBuilder) {

    LIGHT_PURPLE("&dRóżowy", SlotPos(1, 1), 1000, ItemBuilder(Material.INK_SACK, 1, 9, " &8*  &dRózowy  &8*")),
    DARK_PURPLE("&5Fioletowy", SlotPos(1, 2), 2000, ItemBuilder(Material.INK_SACK, 1, 5, " &8*  &5Fioletowy  &8*")),
    GREEN("&aJasny zielony", SlotPos(1, 3),3000, ItemBuilder(Material.INK_SACK, 1, 10, " &8*  &aJasny zielony  &8*")),
    GOLD("&6Pomarańczowy", SlotPos(1, 4), 4000, ItemBuilder(Material.INK_SACK, 1, 15, " &8*  &aPomarańczowy  &8*")),
    YELLOW("&eZółty", SlotPos(1, 5), 5000, ItemBuilder(Material.INK_SACK, 1, 11, " &8*  &eZółty  &8*")),
    AQUA("&aJasny niebieski", SlotPos(1, 6),6000, ItemBuilder(Material.INK_SACK, 1, 12, " &8*  &aJasny niebieski  &8*")),
    DARK_AQUA("&aCiemy niebieski", SlotPos(1, 7), 7000, ItemBuilder(Material.INK_SACK, 1, 6, " &8*  &aCiemy niebieski  &8*"))

}