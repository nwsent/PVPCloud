package pl.pvpcloud.event.managers

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.helpers.ItemEnchantedHelper
import pl.pvpcloud.common.helpers.ItemHelper
import pl.pvpcloud.event.EventPlugin
import pl.pvpcloud.event.events.Event
import pl.pvpcloud.event.events.EventInfoTask
import pl.pvpcloud.event.host.Kit
import java.util.concurrent.ConcurrentHashMap

class EventManager(private val plugin: EventPlugin) {

    var activeEvent: Event? = null

    val kits = ConcurrentHashMap<String, Kit>()

    init {
        EventInfoTask(this.plugin)

        kits["1"] =
                Kit(
                        ItemHelper("Kit - 1", Material.IRON_SWORD.id, 1, 0, arrayListOf("DIAX SET IRON MIECZ 1 REF")).toItemStack(),
                        arrayOf(
                                ItemStack(Material.DIAMOND_BOOTS),
                                ItemStack(Material.DIAMOND_LEGGINGS),
                                ItemStack(Material.DIAMOND_CHESTPLATE),
                                ItemStack(Material.DIAMOND_HELMET)
                        ),
                        arrayOf(
                                ItemStack(Material.IRON_SWORD),
                                ItemStack(Material.GOLDEN_APPLE),
                                ItemStack(Material.COOKED_BEEF, 32)
                        )
                )
        kits["2"] =
                Kit(
                        ItemHelper("Kit - 2", Material.WOOD_SWORD.id, 1, 0, arrayListOf("SKÓRA SET DREWNO MIECZ 1 REF")).toItemStack(),
                        arrayOf(
                                ItemStack(Material.LEATHER_BOOTS),
                                ItemStack(Material.LEATHER_LEGGINGS),
                                ItemStack(Material.LEATHER_CHESTPLATE),
                                ItemStack(Material.LEATHER_HELMET)
                        ),
                        arrayOf(
                                ItemStack(Material.WOOD_SWORD),
                                ItemStack(Material.GOLDEN_APPLE),
                                ItemStack(Material.COOKED_BEEF, 32)
                        )
                )
        kits["3"] =
                Kit(
                        ItemHelper("Kit - 3", Material.IRON_SWORD.id, 1, 0, arrayListOf("IRON SET IRON MIECZ 2 REFY")).toItemStack(),
                        arrayOf(
                                ItemStack(Material.IRON_BOOTS),
                                ItemStack(Material.IRON_LEGGINGS),
                                ItemStack(Material.IRON_CHESTPLATE),
                                ItemStack(Material.IRON_HELMET)
                        ),
                        arrayOf(
                                ItemStack(Material.IRON_SWORD),
                                ItemStack(Material.GOLDEN_APPLE, 2),
                                ItemStack(Material.COOKED_BEEF, 32)
                        )
                )
        kits["4"] =
                Kit(
                        ItemEnchantedHelper("Kit - 4", Material.IRON_SWORD.id, 1, 0, arrayListOf("IRON(PROT 4) SET IRON MIECZ(SHARP 4) 1 REF")).toItemStack(),
                        arrayOf(
                                ItemEnchantedHelper("", Material.IRON_BOOTS.id, 1, 0, arrayListOf(), hashMapOf(Pair(0, 4))).toItemStack(),
                                ItemEnchantedHelper("", Material.IRON_LEGGINGS.id, 1, 0, arrayListOf(), hashMapOf(Pair(0, 4))).toItemStack(),
                                ItemEnchantedHelper("", Material.IRON_CHESTPLATE.id, 1, 0, arrayListOf(), hashMapOf(Pair(0, 4))).toItemStack(),
                                ItemEnchantedHelper("", Material.IRON_HELMET.id, 1, 0, arrayListOf(), hashMapOf(Pair(0, 4))).toItemStack()
                        ),
                        arrayOf(
                                ItemEnchantedHelper("", Material.IRON_SWORD.id, 1, 0, arrayListOf(), hashMapOf(Pair(16, 4))).toItemStack(),
                                ItemStack(Material.GOLDEN_APPLE),
                                ItemStack(Material.COOKED_BEEF, 32)
                        )
                )
    }

}