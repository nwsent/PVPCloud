package pl.pvpcloud.modules.drop

import org.bukkit.Bukkit
import pl.pvpcloud.bootstrap.BootstrapPlugin
import pl.pvpcloud.bootstrap.module.PluginModule
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.modules.drop.config.DropConfig
import pl.pvpcloud.modules.drop.handler.DropHandler
import pl.pvpcloud.modules.drop.structure.DropManage

class DropModule(plugin: BootstrapPlugin) : PluginModule(plugin) {

    private lateinit var config: DropConfig
    private lateinit var manager: DropManage

    override fun start() {
        this.config = ConfigLoader.load(this.folder, DropConfig::class, "drop-config")
        this.manager = DropManage(this.config)

        Bukkit.getPluginManager().registerEvents(DropHandler(this.config, this.manager), this.plugin)

        this.logger.info("Module has been enabled.")
    }

    override fun stop() {
        this.logger.info("Module has been disabled.")
    }

}