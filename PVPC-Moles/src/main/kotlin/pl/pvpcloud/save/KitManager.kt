package pl.pvpcloud.save

import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.moles.MolesPlugin
import java.io.File

class KitManager(private val plugin: MolesPlugin) {

    private val kits = HashSet<Kit>()
    val kitsFolder = File(this.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun getKit(name: String): Kit? {
        return this.kits.singleOrNull { it.name == name }
    }

    fun getKitVersion(name: String, version: Int): Kit? {
        return this.kits.singleOrNull { it.name == name && it.version == version }
    }

    fun getKitsName(): List<String> {
        return this.kits.map { it.name }
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

    fun loadKits() {
        this.kits.clear()
        this.kitsFolder.listFiles()
                .filter { !it.name.contains("example") }
                .forEach {
                    val kitConfig = ConfigLoader.getGson().fromJson(it.readText(), KitConfig::class.java)
                    val kit = Kit(
                            kitConfig.name,
                            kitConfig.version,
                            SerializerHelper.deserializeInventory(kitConfig.inventory),
                            SerializerHelper.deserializeInventory(kitConfig.armor)
                    )
                    this.addKit(kit)
                    this.plugin.logger.info("Load kit ${kit.name}")
                }
    }
}