package pl.pvpcloud.guild.impl

import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.guild.api.GuildsAPI

class GuildsPlugin : CloudPlugin() {

    private val bootstrap = GuildsBootstrap(this)

    override fun onEnable() {
        this.initCommands()

        this.bootstrap.start()

        GuildsAPI.instance = this.bootstrap
    }

    override fun onDisable() {
        this.bootstrap.stop()
    }

}