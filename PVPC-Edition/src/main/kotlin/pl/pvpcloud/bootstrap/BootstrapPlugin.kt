package pl.pvpcloud.bootstrap

import org.bukkit.plugin.java.JavaPlugin
import pl.pvpcloud.modules.drop.DropModule
import java.util.stream.Stream

class BootstrapPlugin : JavaPlugin() {

    private lateinit var dropModule: DropModule

    override fun onEnable() {
        this.dropModule = DropModule(this)

        Stream.of(this.dropModule).forEach {
            it.start()
        }

        this.logger.info("Plugin has been enabled.")
    }

    override fun onDisable() {
        Stream.of(this.dropModule).forEach {
            it.stop()
        }

        this.logger.info("Plugin has been disabled.")
    }

}