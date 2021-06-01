package pl.pvpcloud.save

import org.bukkit.entity.Player
import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.common.configuration.ConfigLoader
import java.io.File

class KitManager(private val plugin: CastlePlugin) {

    private val kits = HashSet<Kit>()
    val kitsFolder = File(this.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun getKit(name: String, kitType: KitType): Kit? {
        return this.kits.singleOrNull { it.name == name && it.kitType == kitType }
    }

    fun getKitVersion(name: String, kitType: KitType, version: Int): Kit? {
        return this.kits.singleOrNull { it.name == name && it.kitType == kitType && it.version == version }
    }

    fun removeKit(kit: Kit) {
        this.kits.remove(kit)
    }

    fun addKit(kit: Kit) {
        this.kits.add(kit)
    }

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
                            kitConfig.version,
                            kitConfig.kitType,
                            SerializerHelper.deserializeInventory(kitConfig.inventory),
                            SerializerHelper.deserializeInventory(kitConfig.armor)
                    )
                    this.addKit(kit)
                    this.plugin.logger.info("Load kit ${kit.name} ${kit.kitType}")
                }
    }
}