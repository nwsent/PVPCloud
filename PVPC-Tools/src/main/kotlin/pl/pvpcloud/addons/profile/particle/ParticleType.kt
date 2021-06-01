package pl.pvpcloud.addons.profile.particle

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import pl.pvpcloud.common.builders.ItemBuilder
import pl.pvpcloud.common.helpers.ItemHelper

enum class ParticleType(val type: String, val slotPos: SlotPos, val itemHelper: ItemBuilder) {

    FIRE("&aOgień", SlotPos(2,2), ItemBuilder(Material.FLINT_AND_STEEL, "&8* &aOgień &8*")),
    HEART("&cSerca", SlotPos(2,3), ItemBuilder(Material.RED_ROSE, "&8* &cSerca &8*")),
    NOTE("&bNutki", SlotPos(2,5), ItemBuilder(Material.JUKEBOX, "&8* &bNutki &8*")),
    CLOUDS("&9Chmurki", SlotPos(2,6), ItemBuilder(Material.STRING, "&8* &9Chmurki &8*")),
    SLIME("&2Slime", SlotPos(3,3), ItemBuilder(Material.SLIME_BALL, "&8* &2Slime &8*")),
    SNOW("&3Snieg", SlotPos(3,5), ItemBuilder(Material.SNOW_BALL, "&8* &3Snieg &8*"))

}