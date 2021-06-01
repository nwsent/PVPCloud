package pl.pvpcloud.ffa.kit

import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.ffa.save.SaveModule
import java.io.File

class KitManager(private val saveModule: SaveModule) {

    val kits = HashSet<Kit>()
    val kitsFolder = File(this.saveModule.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun getKit(name: String) =
        this.kits.singleOrNull { it.name == name }

    fun giveKit(player: Player, kit: Kit) {
        player.inventory.armorContents = kit.armor
        player.inventory.contents = kit.inventory
    }

    private fun loadKits() {
        this.kitsFolder.listFiles()
            .filter { !it.name.contains("example") }
            .forEach {
                val kitConfig = ConfigLoader.getGson().fromJson(it.readText(), KitConfig::class.java)
                val kit = Kit(
                        kitConfig.name,
                        InventorySerializerHelper.deserializeInventory(kitConfig.inventory),
                        InventorySerializerHelper.deserializeInventory(kitConfig.armor)
                )
                this.kits.add(kit)
                this.saveModule.plugin.logger.info("Load kit ${kit.name}")
            }
    }
}