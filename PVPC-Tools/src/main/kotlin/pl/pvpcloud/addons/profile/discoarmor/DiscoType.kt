package pl.pvpcloud.addons.profile.discoarmor

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.builders.ItemBuilder

enum class DiscoType(val slotPos: SlotPos, val itemBuilder: ItemBuilder) {

    RANDOM(SlotPos(2,2), ItemBuilder(Material.LEATHER_CHESTPLATE, "&8*  &eRANDOM  &8*")),
    ULTRA(SlotPos(2,3), ItemBuilder(Material.LEATHER_CHESTPLATE, "&8*  &eULTRA  &8*")),
    SMOOTH(SlotPos(2,5), ItemBuilder(Material.LEATHER_CHESTPLATE, "&8*  &eSMOOTH  &8*")),
    GRAY(SlotPos(2,6), ItemBuilder(Material.LEATHER_CHESTPLATE, "&8*  &eGRAY  &8*"))
 //   POLICE(SlotPos(3,3)),
  //  FLEX(SlotPos(3,5))
}