package pl.pvpcloud.save

import pl.pvpcloud.moles.MolesPlugin

class KitModule(val plugin: MolesPlugin) {

    val kitManager = KitManager(this.plugin)

    init {
        KitAPI.kitModule = this

        this.plugin.registerCommands(
                KitCommand(this)
        )
    }
}