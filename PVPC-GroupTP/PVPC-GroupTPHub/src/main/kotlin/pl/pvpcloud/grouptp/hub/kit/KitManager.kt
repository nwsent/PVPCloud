package pl.pvpcloud.grouptp.hub.kit

import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import java.io.File

class KitManager(private val plugin: GroupTpPlugin) {

    val kits = HashSet<Kit>()
    val kitsFolder = File(this.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun getKit(name: String) =
        this.kits.singleOrNull { it.name == name }

    fun giveKit(player: Player, kit: Kit) {
        player.inventory.armorContents = kit.getArmor()
        player.inventory.contents = kit.getInventory()
    }

    private fun loadKits() {
        this.kitsFolder.listFiles()
            .filter { !it.name.contains("example") }
            .forEach {
                val kit = ConfigLoader.getGson().fromJson(it.readText(), Kit::class.java)
                this.kits.add(kit)
                this.plugin.logger.info("Load kit ${kit.name}")
            }
    }
}