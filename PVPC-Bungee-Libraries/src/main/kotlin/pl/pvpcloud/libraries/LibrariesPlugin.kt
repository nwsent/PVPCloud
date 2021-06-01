package pl.pvpcloud.libraries

import net.md_5.bungee.api.plugin.Plugin

class LibrariesPlugin : Plugin() {

    override fun onEnable() {
        this.logger.info("Libraries load!")
    }
}