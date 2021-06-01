package pl.pvpcloud.libraries

import org.bukkit.plugin.java.JavaPlugin

class LibrariesPlugin : JavaPlugin() {

    override fun onEnable() {
        this.logger.info("Libraries load!")
    }
}