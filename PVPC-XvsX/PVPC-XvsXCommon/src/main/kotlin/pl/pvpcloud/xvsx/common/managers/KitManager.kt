package pl.pvpcloud.xvsx.common.managers

import fr.minuskube.inv.content.SlotPos
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.xvsx.common.kit.Kit
import pl.pvpcloud.xvsx.common.kit.KitEquipment
import pl.pvpcloud.xvsx.common.kit.KitSettings
import java.util.concurrent.ConcurrentHashMap

class KitManager {

    val kits = ConcurrentHashMap<String, Kit>()

    init {
        kits["Iron"] = Kit(
                "Iron",
                SlotPos(1, 1),
                ItemHelper("&cIron", Material.IRON_SWORD.id, 1).toItemStack(),
                KitEquipment(
                        arrayOf(
                                ItemStack(Material.IRON_HELMET, 1)
                        ),
                        arrayOf(
                                ItemStack(Material.IRON_SWORD, 1)
                        )
                ),
                KitSettings(
                        isBuild = false,
                        healthRegeneration = true,
                        showHealth = false,
                        hitDelay = 20,
                        excludedArenas = arrayListOf("sumo1"),
                        arenaWhiteList = arrayListOf()
                )
        )
    }

    fun getKit(name: String) =
            this.kits[name] ?:
                    throw NullPointerException("kit no exist")

}