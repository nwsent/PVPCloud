package pl.pvpcloud.moles.kit

import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.moles.MolesPlugin
import java.io.File

class KitManager(private val plugin: MolesPlugin) {

    val kits = LinkedHashSet<KitProfile>()
    val kitsFolder = File(this.plugin.dataFolder, "kits")

    init {
        if (!this.kitsFolder.exists()) {
            this.kitsFolder.mkdirs()
        }
        loadKits()
    }

    fun getKitProfile(name: String): KitProfile? {
        return this.kits.singleOrNull { it.name == name }
    }

    fun addKit(name: String, kit: Kit) {
        val kitProfile = this.getKitProfile(name)
                ?: throw NullPointerException("kitProfile null")
        kitProfile.kits.add(kit)
    }

    fun giveKit(player: Player, kit: Kit) {
        player.inventory.armorContents = kit.armor
        player.inventory.contents = kit.inventory
    }

    fun loadKits() {
        this.kits.clear()
        this.plugin.configKits.kitsProfiles.forEach {
            this.kits.add(it)
        }
        for (it in this.kitsFolder.listFiles()
                .filter { it.isDirectory }) {
            val kitProfile = this.getKitProfile(it.name)
                    ?: continue
            it.listFiles()
                    .filter { !it.isDirectory }
                    .forEach {
                        val kitConfig = ConfigLoader.getGson().fromJson(it.readText(), KitDataConfig::class.java)
                        val kit = Kit(
                                kitConfig.name,
                                InventorySerializerHelper.deserializeInventory(kitConfig.inventory),
                                InventorySerializerHelper.deserializeInventory(kitConfig.armor)
                        )
                        kitProfile.kits.add(kit)
                        this.plugin.logger.info("Load kit ${kit.name} for ${kitProfile.name}")
                    }
        }
    }
}