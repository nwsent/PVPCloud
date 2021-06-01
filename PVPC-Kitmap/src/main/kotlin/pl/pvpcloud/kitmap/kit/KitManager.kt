package pl.pvpcloud.kitmap.kit

import org.bukkit.inventory.PlayerInventory
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.kitmap.KitmapPlugin
import java.io.File

class KitManager(private val plugin: KitmapPlugin) {

    private val kits = hashSetOf<Kit>()
    private val kitsFolder = File(this.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun addKit(kit: Kit) = this.kits.add(kit)

    fun removeKitByName(name: String) =
        this.getKitByName(name).also {
            this.kits.remove(it)
        }

    fun getKitByName(name: String) = this.getKitBy { it.name == name }

    fun giveKitByName(player: PlayerInventory, kitName: String) =
        this.getKitByName(kitName).also {
            val kit = it ?: return@also

            player.armorContents = InventorySerializerHelper.deserializeInventory(kit.armor)
            player.contents = InventorySerializerHelper.deserializeInventory(kit.inventory)
        }


    private fun getKitBy(kit: (Kit) -> Boolean) = this.kits.find(kit)

    private fun loadKits() =
        this.kitsFolder.listFiles()
                .map { ConfigLoader.getGson().fromJson(it.readText(), Kit::class.java) }
                .forEach {
                    this.addKit(Kit(it.name, it.nameVillager, it.armor, it.inventory))
                    this.plugin.logger.info("Load kit.. (${it.name}")
                }

}