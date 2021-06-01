package pl.pvpcloud.ffa.limits

import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.ffa.FFAPlugin

class LimitsModule(val plugin: FFAPlugin) {

    val config = ConfigLoader.load(this.plugin.dataFolder, LimitsConfig::class, "limits")

    val limitsManager = LimitsManager(this)

    init {
        this.plugin.registerListeners(
            LimitsListener(this)
        )
        this.plugin.registerCommands(
            LimitsCommand(this)
        )
    }
}