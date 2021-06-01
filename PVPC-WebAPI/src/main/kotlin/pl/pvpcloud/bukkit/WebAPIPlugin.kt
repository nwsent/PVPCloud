package pl.pvpcloud.bukkit

import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.web.WebApplication

class WebAPIPlugin : CloudPlugin() {

    lateinit var application: WebApplication

    override fun onEnable() {
        this.application = WebApplication(this)
    }

    override fun onDisable() {
        this.application.http.stop()
    }
}