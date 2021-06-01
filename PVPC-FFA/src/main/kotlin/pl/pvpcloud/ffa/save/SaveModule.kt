package pl.pvpcloud.ffa.save

import pl.pvpcloud.ffa.FFAPlugin
import pl.pvpcloud.ffa.kit.KitCommand
import pl.pvpcloud.ffa.kit.KitManager

class SaveModule(val plugin: FFAPlugin) {

    val kitManager = KitManager(this)
    val saveManager = SaveManager(this)

    init {
        this.plugin.registerCommands(
                KitCommand(this),
                SaveCommand(this)
        )

        this.plugin.registerListeners(
                SaveListener(this)
        )

        SaveAPI.saveModule = this
    }
}