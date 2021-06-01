package pl.pvpcloud.bootstrap.module

import pl.pvpcloud.bootstrap.BootstrapPlugin
import java.io.File
import java.util.logging.Logger

abstract class PluginModule(val plugin: BootstrapPlugin) {

    val logger: Logger
        get() = this.plugin.logger

    val folder: File
        get() = this.plugin.dataFolder

    abstract fun start()
    abstract fun stop()

}