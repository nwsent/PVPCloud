package pl.pvpcloud.save

import pl.pvpcloud.castle.CastlePlugin
import pl.pvpcloud.common.configuration.ConfigLoader

class SaveModule(val plugin: CastlePlugin) {

    var saveConfig = ConfigLoader.load(this.plugin.dataFolder, SaveConfig::class, "saveSections")
    val kitManager = KitManager(plugin)
    val saveManager = SaveManager(this)

    init {
        KitAPI.saveModule = this
        SaveAPI.saveModule = this

        this.plugin.registerCommands(
                KitCommand(this)
        )
    }
}