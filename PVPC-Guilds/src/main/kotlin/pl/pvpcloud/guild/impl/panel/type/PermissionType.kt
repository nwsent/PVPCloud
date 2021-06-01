package pl.pvpcloud.guild.impl.panel.type

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.builders.ItemBuilder

enum class PermissionType(val designation: String, val slotPos: SlotPos, val itemBuilder: ItemBuilder) {

    //Guild
    GUILD_PVP("&ePVP", SlotPos(1, 3), ItemBuilder(Material.PAPER, " &8*  &ePVP  &8*")),
    GUILD_INVITE("&eZapraszanie", SlotPos(1, 4), ItemBuilder(Material.PAPER, " &8*  &eZapraszanie  &8*")),
    GUILD_KICK("&eWyrzucanie", SlotPos(1, 5), ItemBuilder(Material.PAPER, " &8*  &eWyrzucanie  &8*")),

    //Ally
    ALLY_INVITE("&eZapraszanie sojuszu", SlotPos(4, 3), ItemBuilder(Material.PAPER, " &8*  &eZapraszanie sojuszu  &8*")),
    ALLY_KICK("&eZerwanie sojuszu", SlotPos(4, 4), ItemBuilder(Material.PAPER, " &8*  &eZerwanie sojuszu  &8*")),
    ALLY_ACCEPT("&eAktceptowanie sojuszu", SlotPos(4, 5), ItemBuilder(Material.PAPER, "&8*  &eAkceptowanie sojuszu  &8*"))

}